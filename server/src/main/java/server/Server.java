package server;

import dataAccess.*;
import spark.*;
import server;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        Handler myHandler = new Handler(userDAO, authDAO, gameDAO);

        //Endpoints (these point to handlers)
        Spark.delete("/db", this::clear);
        Spark.post("/pet", this::addPet); // Endpoint points to the handlers

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request request, Response response) {

    }

}
