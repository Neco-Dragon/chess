package serviceTests;

import chess.ChessGame;
import Exceptions.*;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;
import RequestClasses.CreateGameRequest;
import RequestClasses.JoinGameRequest;
import RequestClasses.ListGamesRequest;
import ResultClasses.InsertGameResult;
import ResultClasses.ListGamesResult;

import java.util.ArrayList;


public class GameServiceTests {
    //We use memory versions to test the services.
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);
    GameService gameService = new GameService(authDAO, gameDAO);

    public GameServiceTests() throws DataAccessException {
    }

    @BeforeEach
    void clear() throws BadRequestException, DataAccessException {
        clearService.clear();
    }

    @Test
    void insertGameSuccessTest() throws DataAccessException, BadRequestException, UnauthorizedException, AlreadyTakenException {
        authDAO.insertAuth(new AuthData("AuthToken", "MyUsername"));
        InsertGameResult result = gameService.insertGame("AuthToken", new CreateGameRequest("myGame"));
        InsertGameResult expected = new InsertGameResult(result.gameID());
        Assertions.assertEquals(result, expected);
    }
    @Test
    void insertGameFailureTest() throws DataAccessException, BadRequestException, UnauthorizedException, AlreadyTakenException {
        authDAO.insertAuth(new AuthData("AuthToken", "MyUsername"));
        CreateGameRequest request = new CreateGameRequest("myGame");
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.insertGame("InvalidAuthToken", request));
    }

    @Test
    void joinGameSuccessTest() throws DataAccessException, BadRequestException, UnauthorizedException, AlreadyTakenException {
        ChessGame chessGame = new ChessGame();
        authDAO.insertAuth(new AuthData("AuthToken", "MyUsername"));
        GameData testGame = new GameData(1, null, null, "myGame", chessGame);
        gameDAO.insertGame(testGame);
        GameData expectedGame = new GameData(1, "MyUsername", null, "myGame", chessGame);
        gameService.joinGame("AuthToken", new JoinGameRequest(ChessGame.TeamColor.WHITE, 1));
        Assertions.assertEquals(gameDAO.getGameData(1), expectedGame);
    }
    @Test
    void joinGameFailureTest() throws DataAccessException, BadRequestException, AlreadyTakenException {
        ChessGame chessGame = new ChessGame();
        authDAO.insertAuth(new AuthData("AuthToken", "MyUsername"));
        GameData testGame = new GameData(1, "occupied1", "occupied2", "myGame", chessGame);
        gameDAO.insertGame(testGame);
        //usernames already taken
        Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.joinGame("AuthToken", new JoinGameRequest(ChessGame.TeamColor.WHITE, 1)));
        Assertions.assertThrows(AlreadyTakenException.class, () -> gameService.joinGame("AuthToken", new JoinGameRequest(ChessGame.TeamColor.BLACK, 1)));
        //invalid game id
        Assertions.assertThrows(BadRequestException.class, () -> gameService.joinGame("AuthToken", new JoinGameRequest(ChessGame.TeamColor.WHITE, 2)));
        //authTokenInvalid
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.joinGame("InvalidAuthToken", new JoinGameRequest(ChessGame.TeamColor.WHITE, 1)));
    }
    @Test
    void listGameSuccessTest() throws DataAccessException, BadRequestException, UnauthorizedException, AlreadyTakenException {
        authDAO.insertAuth(new AuthData("AuthToken", "MyUsername"));
        ListGamesResult gameList = gameService.listGames(new ListGamesRequest("AuthToken"));
        ListGamesResult expectedResult = new ListGamesResult(new ArrayList<>());
        Assertions.assertEquals(gameList, expectedResult);
    }
    @Test
    void listGameFailureTest() throws DataAccessException, BadRequestException, UnauthorizedException, AlreadyTakenException {
        authDAO.insertAuth(new AuthData("AuthToken", "MyUsername"));
        ListGamesRequest request = new ListGamesRequest("InvalidAuthToken");
        //invalid auth token
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.listGames(request));
    }
}
