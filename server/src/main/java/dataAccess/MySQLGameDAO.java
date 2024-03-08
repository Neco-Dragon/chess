package dataAccess;

import Exceptions.AlreadyTakenException;
import Exceptions.BadRequestException;
import Exceptions.DataAccessException;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256),
              `game` TEXT NOT NULL,
              PRIMARY KEY (gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
    public MySQLGameDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }
    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE games;"; // "DROP TABLE games"
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }

    }

    @Override
    public GameData insertGame(GameData gameData) throws DataAccessException, AlreadyTakenException {
        //int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        int id = gameData.gameID();
        String wu = gameData.whiteUsername();
        String bu = gameData.blackUsername();
        String gn = gameData.gameName();
        String game = new Gson().toJson(gameData.game());
        String createString = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(createString)) {
                ps.setInt(1, id); //plug in gameID
                ps.setString(2, wu); //plug in whiteusername
                ps.setString(3, bu); //plug in blackusername
                ps.setString(4, gn); //plug in gn
                ps.setString(5, game); //plug in game
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
        return gameData;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException, BadRequestException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGameData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null; //This needs to stay here to maintain the expectation throughout the code that no user data existing will return null

    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(readGameData(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return games;
    }

    @Override
    public void joinGame(int gameID, ChessGame.TeamColor clientColor, String clientUsername) throws BadRequestException, DataAccessException, AlreadyTakenException {
        String createString;
        if (clientColor == ChessGame.TeamColor.WHITE){
            createString = "UPDATE games SET whiteUsername = ? WHERE gameID = ?;";
        } else if (clientColor == ChessGame.TeamColor.BLACK) {
            createString = "UPDATE games SET blackUsername = ? WHERE gameID = ?;";
        } else { //observer idempotence
            return;
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(createString)) {
                ps.setString(1, clientUsername);
                ps.setInt(2, gameID);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
    }
    public GameData readGameData(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var JsonString = rs.getString("game");
        var game = new Gson().fromJson(JsonString, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
