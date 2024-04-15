package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
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
