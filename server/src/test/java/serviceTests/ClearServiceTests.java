package serviceTests;

import Exceptions.*;
import chess.ChessGame;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.ClearService;

public class ClearServiceTests {
    //We use memory versions to test the services.
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    ClearService service = new ClearService(authDAO, userDAO, gameDAO);
    @Test
    void clearTest() throws DataAccessException, AlreadyTakenException, BadRequestException {
        GameData testGame = new GameData(1, "Magnus", "Hikaru", "Tata Steel 2024", new ChessGame());
        gameDAO.insertGame(testGame);
        AuthData testAuth = new AuthData("SECURE", "MyUsername");
        authDAO.insertAuth(testAuth);
        UserData testUser = new UserData("MyUsername", "1234", "My@email.com");
        userDAO.createUser(testUser);
        service.clear();
        Assertions.assertNull(gameDAO.getGameData(testGame.gameID()));
        Assertions.assertNull(authDAO.getAuth(testAuth.authToken()));
        Assertions.assertNull(userDAO.getUserData(testUser.username()));
    }
}
