package server;

import Exceptions.*;
import RequestClasses.*;
import ResultClasses.*;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class Handler {
    private final UserService userService;
    private final ClearService clearService;
    private final GameService gameService;
    private final Gson gson = new Gson();

    public Handler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userService = new UserService(authDAO, userDAO);
        this.gameService = new GameService(authDAO, gameDAO);
        this.clearService = new ClearService(authDAO, userDAO, gameDAO);
    }
    public Object clear(Request request, Response response) throws DataAccessException {
        this.clearService.clear();
        response.status(200);
        return "{}";
    }

    public Object register(Request request, Response response) throws DataAccessException, BadRequestException, AlreadyTakenException, UnauthorizedException {
        RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
        RegisterResult result = this.userService.register(registerRequest);
        response.status(200);
        return gson.toJson(result);
    }

    public Object login(Request request, Response response) throws BadRequestException, DataAccessException, UnauthorizedException, AlreadyTakenException {
        LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);
        LoginResult result = this.userService.login(loginRequest);
        response.status(200);
        return gson.toJson(result);
    }

    public Object logout(Request request, Response response) throws BadRequestException, DataAccessException, UnauthorizedException {
        String authToken = request.headers("authorization");
        this.userService.logout(new LogoutRequest(authToken));
        response.status(200);
        return "{}"; //This handle request has nothing in its return body.
    }

    public Object listGames(Request request, Response response) throws UnauthorizedException, DataAccessException, BadRequestException {
        String authToken = request.headers("authorization");
        ListGamesResult result = this.gameService.listGames(new ListGamesRequest(authToken));
        response.status(200);
        return gson.toJson(result);
    }

    public Object createGame(Request request, Response response) throws UnauthorizedException, DataAccessException, AlreadyTakenException, BadRequestException {
        String authToken = request.headers("authorization");
        CreateGameRequest createGameRequest = gson.fromJson(request.body(), CreateGameRequest.class);
        InsertGameResult result = this.gameService.insertGame(authToken, createGameRequest);
        response.status(200);
        return gson.toJson(result);
    }

    public Object joinGame(Request request, Response response) throws UnauthorizedException, BadRequestException, DataAccessException, AlreadyTakenException {
        String authToken = request.headers("authorization");
        JoinGameRequest joinGameRequest = gson.fromJson(request.body(), JoinGameRequest.class);
        this.gameService.joinGame(authToken, joinGameRequest);
        response.status(200);
        return "{}"; //This handle request has nothing in its return body.
    }
}
