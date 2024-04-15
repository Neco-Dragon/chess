package server;

import Exceptions.*;
import dataAccess.*;
import spark.*;


public class Server {

    public int run(int desiredPort){
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        UserDAO userDAO;
        AuthDAO authDAO;
        GameDAO gameDAO;
        Handler myHandler;
        try {
            userDAO = new MySQLUserDAO();
            authDAO = new MySQLAuthDAO();
            gameDAO = new MySQLGameDAO();

            //Exception Handler
            Spark.exception(ServerException.class, this::exceptionHandler);

            myHandler = new Handler(userDAO, authDAO, gameDAO);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }


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
