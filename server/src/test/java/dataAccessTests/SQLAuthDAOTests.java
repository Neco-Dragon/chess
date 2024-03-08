package dataAccessTests;

import Exceptions.DataAccessException;
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
    public void testGenerateAuthToken() throws Exception {
        String authToken = authDAO.generateAuthToken("testUser");

        //the generated authentication token is not null or empty
        Assertions.assertNotNull(authToken);
        Assertions.assertNotEquals("", authToken);
    }
}
