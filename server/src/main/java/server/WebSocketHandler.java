package server;

import Exceptions.*;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

import java.lang.reflect.GenericArrayType;

@WebSocket
public class WebSocketHandler {

    GameDAO gameDAO;
    UserDAO userDAO;
    AuthDAO authDAO;
    //TODO: Also keep a sessions object. Connection Handler?

    public WebSocketHandler() {
        try {
            gameDAO = new MySQLGameDAO();
            userDAO = new MySQLUserDAO();
            authDAO = new MySQLAuthDAO();
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
    }
    private void joinObserverService(Session session, String message) {
    }
    private void makeMoveService(Session session, String message) {
    }
    private void resignService(Session session, String message) {
    }
    private void leaveService(Session session, String message) {
    }


}
