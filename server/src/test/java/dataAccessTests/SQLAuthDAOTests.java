package dataAccessTests;

import Exceptions.AlreadyTakenException;
import Exceptions.BadRequestException;
import Exceptions.DataAccessException;
import Exceptions.UnauthorizedException;
import dataAccess.MySQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.*;

public class SQLAuthDAOTests {
    private static MySQLAuthDAO authDAO;

    @BeforeAll
    public static void setUp() throws Exception {
        authDAO = new MySQLAuthDAO();
    }

    @AfterEach
    public void reset() throws Exception {
        authDAO.clear();
    }

    @Test
    public void testInsertAuth() throws Exception {
        AuthData authData = authDAO.insertAuth(new AuthData("testAuthToken", "testUser"));

        // data is not null
        Assertions.assertNotNull(authData);

        Assertions.assertEquals("testAuthToken", authData.authToken());
        Assertions.assertEquals("testUser", authData.username());
    }
    @Test
    public void testInsertAuthFailure() throws Exception {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.insertAuth(new AuthData(null, "testUser"));
        });
    }

    @Test
    public void testGetAuth() throws Exception {
        AuthData authData = new AuthData("testAuthToken", "testUser");
        authDAO.insertAuth(authData);

        AuthData retrievedAuth = authDAO.getAuth("testAuthToken");

        Assertions.assertNotNull(retrievedAuth);
        Assertions.assertEquals("testAuthToken", retrievedAuth.authToken());
        Assertions.assertEquals("testUser", retrievedAuth.username());
    }

    @Test
    public void testGetAuthNonExistingAuthToken() throws Exception {
        // Try to retrieve non-existing authentication data
        Assertions.assertNull(authDAO.getAuth("nonExistingAuthToken"));
    }
    @Test
    public void testDeleteAuth() throws Exception {
        authDAO.insertAuth(new AuthData("authToken", "Username"));
        Assertions.assertDoesNotThrow(() -> {
            authDAO.deleteAuth("authToken");
        });
    }

    @Test
    public void testDeleteAuthFailure() {
        Assertions.assertDoesNotThrow(() -> {
            authDAO.deleteAuth("nonExistingAuthToken");
        });

    }

    @Test
    public void testGetUsername() throws DataAccessException, AlreadyTakenException, UnauthorizedException, BadRequestException {
        authDAO.insertAuth(new AuthData("authToken", "testUser"));
        Assertions.assertEquals("testUser", authDAO.getUsername("authToken"));
    }

    @Test
    public void testGetUsernameFailure() throws UnauthorizedException, BadRequestException, DataAccessException {
        Assertions.assertThrows(Exception.class, () -> authDAO.getUsername("nonExistingAuthToken"));
    }

    @Test
    public void testGenerateAuthToken() throws Exception {
        String authToken = authDAO.generateAuthToken("testUser");

        Assertions.assertNotNull(authToken);
        Assertions.assertNotEquals("", authToken);
    }
    @Test
    public void testGenerateAuthTokenFailure() throws Exception {
        //Note, it does not really make sense for this method to fail, but this is the expected behavior in the face of unexpected input
        Assertions.assertDoesNotThrow(() -> authDAO.generateAuthToken(""));
    }
}
