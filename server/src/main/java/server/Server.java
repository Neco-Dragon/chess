package server;

import Exceptions.*;
import dataAccess.*;
import spark.*;


public class Server {

    public int run(int desiredPort) throws DataAccessException {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        //TODO: Switch from SQL to MEmory for testing
        UserDAO userDAO = new MySQLUserDAO();
        AuthDAO authDAO = new MySQLAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        //Exception Handler
        Spark.exception(ServerException.class, this::exceptionHandler);

        Handler myHandler = new Handler(userDAO, authDAO, gameDAO);

        //Endpoints (these point to handlers)
        //CLEAR
        Spark.delete("/db", myHandler::clear);
        //REGISTER
        Spark.post("/user", myHandler::register);
        //LOGIN
        Spark.post("/session", myHandler::login);
        //LOGOUT
        Spark.delete("/session", myHandler::logout);
        //LIST GAMES
        Spark.get("/game", myHandler::listGames);
        //CREATE GAME
        Spark.post("/game", myHandler::createGame);
        //JOIN GAME
        Spark.put("/game", myHandler::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ServerException ex, Request request, Response response) {
        response.status(ex.statusCode());
        response.body(ex.getMessage());
    }
}
