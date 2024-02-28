package serviceTests;

import chess.ChessGame;
import dataAccess.*;
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
    void clearTest() throws DataAccessException, BadRequestException {
        GameData testGame = new GameData(1, "Magnus", "Hikaru", "Tata Steel 2024", new ChessGame());
        gameDAO.insertGame(testGame);
        AuthData testAuth = new AuthData("SECURE", "MyUsername");
        authDAO.insertAuth(testAuth);
        UserData testUser = new UserData("MyUsername", "1234", "My@email.com");
        userDAO.createUser(testUser);
        service.clear();
        Assertions.assertThrows(BadRequestException.class, () -> gameDAO.getGame(testGame.gameID()));
        Assertions.assertThrows(BadRequestException.class, () -> authDAO.getAuth(testAuth));
        Assertions.assertThrows(BadRequestException.class, () -> userDAO.getUser(testUser.username()));
    }
}
