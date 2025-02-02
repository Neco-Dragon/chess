package server;

import Exceptions.*;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userGameCommands.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

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
            ChessGame.TeamColor teamColor = joinPlayer.playerColor;

            if (username == null){
                throw new UnauthorizedException("Bad Auth Token");
            }
            if (gameData.game() == null){
                throw new DataAccessException("No such game ID");
            }
            if (teamColor == WHITE){
                if (!Objects.equals(gameData.whiteUsername(), username)){ //TODO: This disallows users to enter a game even if there's a null username, but that's okay since the HTTP request should join the game for them already
                    throw new DataAccessException("White player already taken");
                }
            }
            if (teamColor == ChessGame.TeamColor.BLACK){
                if (!Objects.equals(gameData.blackUsername(), username)){
                    throw new DataAccessException("Black player already taken");
                }
            }

            //add user to the session
            connectionHandler.add(gameID, username, session);

            //Create the messages
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " joined as " + joinPlayer.playerColor.toString());

            //Server sends a LOAD_GAME message back to the root client.
            send(session, new Gson().toJson(loadGame));
            //Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
            broadcast(username, new Gson().toJson(notification), gameData.gameID());

        } catch (Exception e){
            send(session, new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, e.getMessage())));
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
            send(session, new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, e.getMessage())));
        }
    }
    private void makeMoveService(Session session, String message) throws IOException {
        try {
            //Root Client sends MAKE_MOVE
            MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);

            //Access the Data
            ChessMove chessMove = makeMove.getMove();
            int gameID = makeMove.gameID;
            GameData gameData = gameDAO.getGameData(gameID);
            String username = authDAO.getUsername(makeMove.getAuthString());
            ChessGame.TeamColor playerColor = null;

            if (Objects.equals(gameData.whiteUsername(), username)){
                playerColor = WHITE;
            } else if (Objects.equals(gameData.blackUsername(), username)) {
                playerColor = BLACK;
            }
            if (playerColor == null){
                throw new InvalidMoveException("You cannot move pieces if you are observing\"");
            }

            ChessPiece pieceToMove = gameData.game().getBoard().getPiece(chessMove.getStartPosition());
            if (pieceToMove == null){
                throw new InvalidMoveException("That move is invalid");
            }
            if (!pieceToMove.sameTeam(playerColor)){
                throw new InvalidMoveException("You cannot move pieces that aren't yours");
            }
            if (gameData.game().isOver()){
                throw new Exception("This game has already ended");
            }

            //Fulfill the request.
            gameData.game().makeMove(chessMove);
            gameDAO.updateGame(gameData);
            boolean checkmate = gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK) || gameData.game().isInCheckmate(WHITE);
            boolean check = gameData.game().isInCheck(ChessGame.TeamColor.BLACK) || gameData.game().isInCheck(WHITE);

            //Create the messages
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, chessMove.toString());

            //Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
            broadcast("", new Gson().toJson(loadGame), gameID);
            //Server sends a Notification message to all other clients in that game informing them what move was made.
            broadcast(username, new Gson().toJson(notification), gameID);

            //If the move results in check or checkmate the server sends a Notification message to all clients.
            if (checkmate){
                gameData.game().setOver(true);
                gameDAO.updateGame(gameData); //End the game
                broadcast("", new Gson().toJson(new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "This puts the opponent in CHECKMATE!")), gameID);
            } else if (check) {
                broadcast("", new Gson().toJson(new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "This puts the opponent in CHECK!")), gameID);
            }

        } catch (Exception e){
            send(session, new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, e.getMessage())));
        }
    }
    private void resignService(Session session, String message) throws IOException {
        try {
            //Root Client sends RESIGN
            Resign resign = new Gson().fromJson(message, Resign.class);

            //Access the Data
            String username = authDAO.getUsername(resign.getAuthString());
            GameData gameData = gameDAO.getGameData(resign.gameID);

            //DATA VALIDATION
            if (username == null){
                throw new UnauthorizedException("Bad Auth Token");
            }
            if (gameData.game() == null){
                throw new DataAccessException("No such gameID");
            }
            if (!(username.equals(gameData.whiteUsername()) || username.equals(gameData.blackUsername()))){
                throw new Exception("You cannot resign if you are not playing");
            }
            if (gameData.game().isOver()){
                throw new Exception("This game has already ended");
            }


            //Fulfill the request.
            gameData.game().setOver(true); //End the game
            gameDAO.updateGame(gameData); //End the game

            //Create the messages
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has resigned the game");

            //Server sends a Notification message to all clients in that game informing them that the root client resigned. This applies to both players and observers.
            broadcast("", new Gson().toJson(notification), resign.gameID);
        } catch (Exception e){
            send(session, new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, e.getMessage())));
        }
    }
    private void leaveService(Session session, String message) throws IOException {
        try {
            //Root Client sends LEAVE
            Leave leave = new Gson().fromJson(message, Leave.class);

            //Access the Data
            String username = authDAO.getUsername(leave.getAuthString());
            ChessGame game = gameDAO.getGameData(leave.gameID).game();

            //DATA VALIDATION
            if (username == null){
                throw new UnauthorizedException("Bad Auth Token");
            }
            if (game == null){
                throw new DataAccessException("No such gameID");
            }

            //remove the user from the session
           connectionHandler.remove(leave.gameID, new Connection(username, session));

            //Create the messages
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left the session.");

            //Server sends a notification back to all users that the client has left that session.
            broadcast("", new Gson().toJson(notification), leave.gameID);

        } catch (Exception e){
            send(session, new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, e.getMessage())));
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
