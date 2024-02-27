package server;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class Handler {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserService userService;
    ClearService clearService;
    GameService gameService;

    public Handler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userService = new UserService(this.authDAO, this.userDAO);
        this.gameService = new GameService(this.authDAO, this.gameDAO);
        this.clearService = new ClearService(this.authDAO, this.userDAO, this.gameDAO);
        }
    }
    private Object clear(Request request, Response response) {
        try {
            this.clearService.clear();
            return "[200]";
        }
        catch (DataAccessException e){
            return "[500] { \"message\": \"Error: description\" }";
        }
    }

}
