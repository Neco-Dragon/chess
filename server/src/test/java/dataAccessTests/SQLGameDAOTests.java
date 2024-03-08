package dataAccessTests;

import chess.ChessGame;
import dataAccess.MySQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

public class SQLGameDAOTests {
    private static MySQLGameDAO gameDAO;

    @BeforeAll
    public static void setUp() throws Exception {
        gameDAO = new MySQLGameDAO();
    }

    @AfterEach
    public void tearDown() throws Exception {
        gameDAO.clear();
    }

    @Test
    public void testInsertGame() throws Exception {
        GameData gameData = gameDAO.insertGame(new GameData(1, "whiteUsername", "blackUsername", "gameName", new ChessGame()));

        //the inserted game data is not null
        Assertions.assertNotNull(gameData);
        //each attribute correct
        Assertions.assertEquals(1, gameData.gameID());
        Assertions.assertEquals("whiteUsername", gameData.whiteUsername());
        Assertions.assertEquals("blackUsername", gameData.blackUsername());
        Assertions.assertEquals("gameName", gameData.gameName());
    }

    @Test
    public void testGetGame() throws Exception {
        // Insert game data
        GameData gameData = new GameData(1, "whiteUser", "blackUser", "gameName", new ChessGame());
        gameDAO.insertGame(gameData);

        // Retrieve the game data
        GameData retrievedGame = gameDAO.getGame(1);

        // Ensure the retrieved game data is not null
        Assertions.assertNotNull(retrievedGame);
        Assertions.assertEquals(1, retrievedGame.gameID());
        Assertions.assertEquals("whiteUser", retrievedGame.whiteUsername());
        Assertions.assertEquals("blackUser", retrievedGame.blackUsername());
        Assertions.assertEquals("gameName", retrievedGame.gameName());
    }

    @Test
    public void testGetNonExistingGame() throws Exception {
        gameDAO.getGame(9999);
    }

    @Test
    public void testListGames() throws Exception {
        ArrayList<GameData> games = gameDAO.listGames();

        // the list is not null
        Assertions.assertNotNull(games);
        // the list is initially empty
        Assertions.assertEquals(0, games.size());
    }

    @Test
    public void testJoinGame() throws Exception {
        // Insert a game
        GameData gameData = new GameData(1, "whiteUser", "blackUser", "gameName", new ChessGame());
        gameDAO.insertGame(gameData);

        gameDAO.joinGame(1, ChessGame.TeamColor.WHITE, "whiteUser2");
        //retrieve the game after joining
        GameData joinedGame = gameDAO.getGame(1);
        Assertions.assertEquals("whiteUser2", joinedGame.whiteUsername());
    }
}
