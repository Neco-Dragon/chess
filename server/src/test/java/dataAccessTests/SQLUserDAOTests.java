package dataAccessTests;

import Exceptions.DataAccessException;
import dataAccess.MySQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

public class SQLUserDAOTests {
    private static MySQLUserDAO userDAO;

    @BeforeAll
    public static void setUp() throws Exception {
        userDAO = new MySQLUserDAO();
    }

    @AfterEach
    public void reset() throws Exception {
        userDAO.clear();
    }

    @Test
    public void testClearSuccess() throws Exception {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.clear();
        });
    }

    @Test
    public void testCreateUser() throws Exception {
        UserData userData =  userDAO.createUser(new UserData("testUser", "password", "me@example.com"));

        Assertions.assertNotNull(userData);
        Assertions.assertEquals("testUser", userData.username());
        Assertions.assertEquals("password", userData.password());
        Assertions.assertEquals("me@example.com", userData.email());
    }
    @Test
    public void testCreateUserFailure() throws Exception {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(new UserData(null, "password", "me@example.com"));
        });
    }

    @Test
    public void testGetUserData() throws Exception {
        // Create a user
        userDAO.createUser(new UserData("testUser", "password", "me@example.com"));

        UserData retrievedUser = userDAO.getUserData("testUser");

        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals("testUser", retrievedUser.username());
        Assertions.assertEquals("password", retrievedUser.password());
        Assertions.assertEquals("me@example.com", retrievedUser.email());
    }

    @Test
    public void testGetUserDataNonExistingUser() throws Exception {
        // Try to get non-existent user
        Assertions.assertNull(userDAO.getUserData("nonExistingUser"));
    }
}
