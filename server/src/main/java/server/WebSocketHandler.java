package server;

import Exceptions.*;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userGameCommand.*;
import webSocketMessages.serverMessages.*;

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

    private void joinPlayerService(Session session, String message) {
//        JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);

        //Session is the connection. It represents a client.
        //Turn the message into a more specific UserGameCommand object. This is what the client sends to the server
        //Look at WebSocket Interactions
        //TODO: create a LoadGame Websocket message object.
        //TODO: Json magic.
        //TODO: Change message to a load game
        //Root Client sends JOIN_PLAYER
        //
        //Server sends a LOAD_GAME message back to the root client.
        //Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
//        broadcast(session, message); //Notification Message
//        send(session, message); //LoadGame Message

    }
    private void joinObserverService(Session session, String message) {
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

    //TODO: Filter the clients and have it just send it to ones that are in the game
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
