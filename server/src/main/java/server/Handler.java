package server;

import RequestClasses.*;
import ResultClasses.*;
import com.google.gson.Gson;
import dataAccess.*;
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

    public Object register(Request request, Response response) throws DataAccessException {
        RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);
        RegisterResult result = this.userService.register(registerRequest);
        response.status(200);
        return gson.toJson(result);
    }

    public Object login(Request request, Response response) throws BadRequestException, DataAccessException {
        LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);
        LoginResult result = this.userService.login(loginRequest);
        response.status(200);
        return gson.toJson(result);
    }

    public Object logout(Request request, Response response) throws BadRequestException, DataAccessException {
        String authToken = request.headers("authorization");
        this.userService.logout(new LogoutRequest(authToken));
        response.status(200);
        return "{}"; //This handle request has nothing in its return body.
    }

    public Object listGames(Request request, Response response) throws UnauthorizedException, DataAccessException {
        ListGamesRequest listGamesRequest = gson.fromJson(request.body(), ListGamesRequest.class);
        ListGamesResult result = this.gameService.listGames(listGamesRequest);
        response.status(200);
        return gson.toJson(result);
    }

    public Object createGame(Request request, Response response) throws UnauthorizedException, DataAccessException {
        InsertGameRequest insertGameRequest = gson.fromJson(request.body(), InsertGameRequest.class);
        InsertGameResult result = this.gameService.insertGame(insertGameRequest);
        response.status(200);
        return gson.toJson(result);
    }

    public Object joinGame(Request request, Response response) throws UnauthorizedException, BadRequestException, DataAccessException {
        JoinGameRequest joinGameRequest = gson.fromJson(request.body(), JoinGameRequest.class);
        this.gameService.joinGame(joinGameRequest);
        response.status(200);
        return "{}"; //This handle request has nothing in its return body.
    }
}
