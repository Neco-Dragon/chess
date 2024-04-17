package Websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userGameCommands.Leave;
import webSocketMessages.userGameCommands.UserGameCommand;

import javax.websocket.ContainerProvider;
import javax.websocket.MessageHandler;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.net.URISyntaxException;


@WebSocket
public class WebSocketFacade {

    public javax.websocket.Session session;

    public WebSocketFacade() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String s) {
                ServerMessage serverMessage = new Gson().fromJson(s, ServerMessage.class);

                switch (serverMessage.getServerMessageType()){
                    case ServerMessage.ServerMessageType.NOTIFICATION -> notificationService(s);
                    case ServerMessage.ServerMessageType.LOAD_GAME -> loadGameService(s);
                    case ServerMessage.ServerMessageType.ERROR -> errorService(s);
                }
            }
        });
        }


    private void errorService(String message) {
        Error error = new Gson().fromJson(message, Error.class);
        System.out.println(error.getMessage());
    }

    private void loadGameService(String message) {
        LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
        System.out.println(loadGame.game.getBoard().toString());
    }

    private void notificationService(String message){
        Notification notification = new Gson().fromJson(message, Notification.class);
        System.out.println(notification.getMessage());
    }

}
