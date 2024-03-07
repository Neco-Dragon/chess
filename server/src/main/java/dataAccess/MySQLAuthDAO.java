package dataAccess;

import Exceptions.*;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class MySQLAuthDAO implements AuthDAO {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authTokens (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
    public MySQLAuthDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }
    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE authTokens;"; // "DROP TABLE authTokens"
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public AuthData insertAuth(AuthData authData) throws DataAccessException, AlreadyTakenException {
        String t = authData.authToken();
        String u = authData.username();
        String createString = "INSERT INTO authTokens (authToken, username) VALUES (?, ?);";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(createString)) {
                ps.setString(1, t); //plug in authToken
                ps.setString(2, u); //plug in username
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
        return authData;
    }

    private AuthData readAuthData(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(username, authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException, BadRequestException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT authToken, username, FROM authTokens WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuthData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null; //This needs to stay here to maintain the expectation throughout the code that no user data existing will return null
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException, BadRequestException, UnauthorizedException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM authTokens WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException, BadRequestException, UnauthorizedException {
        return getAuth(authToken).username();
    }

    @Override
    public String generateAuthToken(String username){
        //"Now that's what I call hashing"
        Random random = new Random();
        return Long.toHexString(random.nextLong() + System.currentTimeMillis());
    }

}
