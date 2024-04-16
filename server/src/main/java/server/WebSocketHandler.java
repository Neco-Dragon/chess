package server;

import Exceptions.*;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
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
            ChessGame game = this.gameDAO.getGameData(joinPlayer.gameID).game();
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);

            //Server sends a LOAD_GAME message back to the root client.
            send(session, loadGame.toString()); //TODO: What is the message part that we send?
            //Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
            broadcast(username, message, 0); //Notification Message

        } catch (Exception e){
            send(session, new Gson().toJson(new Error(e.getMessage())));
        }

    }
    private void joinObserverService(Session session, String message) throws IOException {
        try {
            JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
            if (authDAO.getAuth(joinObserver.getAuthString()) == null){
                throw new UnauthorizedException("Bad auth token");
            }
            if (gameDAO.getGameData(joinObserver.gameID) == null){
                throw new DataAccessException("No such game");
            }
            LoadGame loadGame =
            this.send(session, );
            //connectionHandler.add();
        } catch (Exception e){
            send(session, new Gson().toJson(new Error(e.getMessage())));
        }
        //Send Jsonified LoadGame back to Root User
        //Send Jsonified Notification back to all other clients


    }
    private void makeMoveService(Session session, String message) {
    }
    private void resignService(Session session, String message) {
    }
    private void leaveService(Session session, String message) {
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
