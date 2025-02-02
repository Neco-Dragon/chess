package server;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler {
    //game ID
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();


    public void add(int gameID, String playerName, Session session) {
        var connection = new Connection(playerName, session);
        var connArr =  connections.get(gameID);
        if (connArr == null){
            connections.put(gameID, new ArrayList<>());
            connArr =  connections.get(gameID);
        }
        connArr.add(connection);
    }
    //Do I have access directly to a connection? Just a session?
    public void remove(int gameID, Connection connection) {
        connections.get(gameID).remove(connection);
    }

}
