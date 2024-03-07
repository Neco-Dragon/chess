package dataAccess;

import Exceptions.BadRequestException;
import Exceptions.DataAccessException;
import Exceptions.UnauthorizedException;
import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO{

    public MySQLUserDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "DROP TABLE users;"; // "TRUNCATE users"
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (var ignored = ps.executeQuery()) {
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        String u = userData.username();
        String p = userData.password();
        String e = userData.email();
        String createString = "INSERT INTO users (username, password, email) VALUES ('?', '?', '?');";
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, u); //plug in username
                ps.setString(2, p); //plug in password
                ps.setString(3, e); //plug in email
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(String.format("Unable to read data: %s", ex.getMessage()));
        }
        return userData;
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException, BadRequestException, UnauthorizedException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username); //TODO: What is this for? Protects against SQL injection attacks
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    private UserData readPass(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }


}
