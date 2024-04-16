package server;

import Exceptions.*;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userGameCommands.*;

import java.io.IOException;
import java.util.ArrayList;

@WebSocket
public class WebSocketHandler {

    GameDAO gameDAO;
    UserDAO userDAO;
    AuthDAO authDAO;
    ConnectionHandler connectionHandler;

    public WebSocketHandler() {
        try {
            gameDAO = new MySQLGameDAO();
            userDAO = new MySQLUserDAO();
            authDAO = new MySQLAuthDAO();
            connectionHandler = new ConnectionHandler();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);

        switch (userGameCommand.getCommandType()){
            case JOIN_PLAYER -> this.joinPlayerService(session, message);
            case JOIN_OBSERVER -> this.joinObserverService(session, message);
            case MAKE_MOVE -> this.makeMoveService(session, message);
            case RESIGN -> this.resignService(session, message);
            case LEAVE -> this.leaveService(session, message);
        }

    }
    //SERVICES WILL CALL THE DAOs

    private void joinPlayerService(Session session, String message) throws IOException {
        try {
            //Root Client sends JOIN_PLAYER
            JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);

            //Access the Data
            String username = this.authDAO.getUsername(joinPlayer.getAuthString());
            GameData gameData = this.gameDAO.getGameData(joinPlayer.gameID);
            int gameID = gameData.gameID();

            //TODO: Data validation

            //add user to the session
            connectionHandler.add(gameID, username, session);

            //Create the messages
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " joined as " + joinPlayer.playerColor.toString());

            //Server sends a LOAD_GAME message back to the root client.
            send(session, new Gson().toJson(loadGame)); //TODO: What is the message part that we send?
            //Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
            broadcast(username, new Gson().toJson(notification), gameData.gameID()); //TODO: 2 arg correct?

        } catch (Exception e){
            send(session, new Gson().toJson(new Error(e.getMessage())));
        }

    }
    private void joinObserverService(Session session, String message) throws IOException {
        try {
            //Root Client sends JOIN_OBSERVER
            JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);

            //Access the Data
            int gameID = joinObserver.gameID;
            String username = authDAO.getUsername(joinObserver.getAuthString());
            GameData gameData = gameDAO.getGameData(gameID);

            //DATA VALIDATION
            if (authDAO.getAuth(joinObserver.getAuthString()) == null){
                throw new UnauthorizedException("Bad auth token");
            }
            if (gameData == null){
                throw new DataAccessException("No such game");
            }

            //Fulfill the request. In this case, we don't change the data at all

            //add user to the session
            connectionHandler.add(gameID, username, session);

            //Create the messages
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,  username + " joined as an observer\n");

            //Server sends a LOAD_GAME message back to the root client.
            send(session, new Gson().toJson(loadGame));
            //Server sends a Notification message to all other clients in that the root client joined as an observer.
            broadcast(username, new Gson().toJson(notification), gameData.gameID());

        } catch (Exception e){
            send(session, new Gson().toJson(new Error(e.getMessage())));
        }
    }
    private void makeMoveService(Session session, String message) throws IOException {
        try {
            //Root Client sends MAKE_MOVE
            MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);

            //Access the Data
            ChessMove chessMove = makeMove.chessMove;
            int gameID = makeMove.gameID;
            GameData gameData = gameDAO.getGameData(gameID);
            String username = authDAO.getUsername(makeMove.getAuthString());

            //DATA VALIDATION

            //Fulfill the request.
            gameData.game().makeMove(chessMove);
            GameData newGameData = gameDAO.getGameData(gameID);
            boolean checkmate = newGameData.game().isInCheckmate(ChessGame.TeamColor.BLACK) || newGameData.game().isInCheckmate(ChessGame.TeamColor.WHITE);
            boolean check = newGameData.game().isInCheck(ChessGame.TeamColor.BLACK) || newGameData.game().isInCheck(ChessGame.TeamColor.WHITE);

            //Create the messages
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, chessMove.toString());

            //Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
            broadcast("", loadGame.toString(), gameID);
            //Server sends a Notification message to all other clients in that game informing them what move was made.
            broadcast(username, notification.getMessage(), gameID);

            //If the move results in check or checkmate the server sends a Notification message to all clients.
            if (checkmate){
                broadcast("", "This puts the opponent in CHECKMATE!", gameID);
            } else if (check) {
                broadcast("", "This puts the opponent in CHECK!", gameID);
            }

        } catch (Exception e){
            send(session, new Gson().toJson(new Error(e.getMessage())));
        }
    }
    private void resignService(Session session, String message) throws IOException {
        try {
            //Root Client sends USERGAMECOMMAND
            JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);

            //Access the Data

            //DATA VALIDATION

            //Fulfill the request.

            //add user to the session

            //Create the messages

            //Server sends a LOAD_GAME message back to the root client.
            //Server sends a Notification message to all other clients in that the root client joined as an observer.

        } catch (Exception e){
            send(session, new Gson().toJson(new Error(e.getMessage())));
        }
    }
    private void leaveService(Session session, String message) throws IOException {
        try {
            //Root Client sends USERGAMECOMMAND
            JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);

            //Access the Data

            //DATA VALIDATION

            //Fulfill the request.

            //add user to the session

            //Create the messages

            //Server sends a LOAD_GAME message back to the root client.
            //Server sends a Notification message to all other clients in that the root client joined as an observer.

        } catch (Exception e){
            send(session, new Gson().toJson(new Error(e.getMessage())));
        }
    }

    private void send(Session session, String message) throws IOException {
        session.getRemote().sendString(message);
    }

    //Filters the clients and has it just send it to ones that are in the game
    private void broadcast(String excludeVisitorName, String message, int gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (Connection c : connectionHandler.connections.get(gameID)) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName)) {
                    c.send(message);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (Connection c : removeList) {
            connectionHandler.connections.get(gameID).remove(c);
        }
    }

}
