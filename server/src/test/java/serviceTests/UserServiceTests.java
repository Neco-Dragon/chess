package serviceTests;

import RequestClasses.LoginRequest;
import RequestClasses.LogoutRequest;
import RequestClasses.RegisterRequest;
import ResultClasses.LoginResult;
import ResultClasses.RegisterResult;
import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.UserService;

public class UserServiceTests {
    //We use memory versions to test the services.
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();
    ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);
    UserService userService = new UserService(authDAO, userDAO);
    @BeforeEach
    void clear() throws BadRequestException, DataAccessException {
        clearService.clear();
    }

    @Test
    void registerSuccessTest() throws DataAccessException, BadRequestException, UnauthorizedException {
        RegisterResult result = userService.register(new RegisterRequest("myUser", "myPass", "me@email.com"));
        Assertions.assertEquals(result.username(), "myUser");
        Assertions.assertDoesNotThrow(() -> authDAO.confirmAuth(result.authToken()));
    }
    @Test
    void registerFailureTest() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("myUser", "myPass", "me@email.com");
        userService.register(request);
        //user already taken
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(request));
    }

    @Test
    void loginSuccessTest() throws BadRequestException, DataAccessException {
        userService.register(new RegisterRequest("myUser", "myPass", "me@email.com"));
        Assertions.assertDoesNotThrow(() -> userService.login(new LoginRequest("myUser", "myPass")));
    }

    @Test
    void loginFailureTest() throws DataAccessException, BadRequestException {
        userService.register(new RegisterRequest("myUser", "myPass", "me@email.com"));
        //wrong username
        Assertions.assertThrows(BadRequestException.class, () -> userService.login(new LoginRequest("myWrongUser", "myPass")));
        //right username, wrong password
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(new LoginRequest("myUser", "myWrongPass")));
    }
    @Test
    void logoutSuccessTest() throws DataAccessException, BadRequestException {
        userService.register(new RegisterRequest("myUser", "myPass", "me@email.com"));
        LoginResult result = userService.login(new LoginRequest("myUser", "myPass"));
        Assertions.assertDoesNotThrow(() -> userService.logout(new LogoutRequest(result.authToken())));
    }

    @Test
    void logoutFailureTest() throws DataAccessException, BadRequestException {
        userService.register(new RegisterRequest("myUser", "myPass", "me@email.com"));
        userService.login(new LoginRequest("myUser", "myPass"));
        Assertions.assertThrows(BadRequestException.class, () -> userService.logout(new LogoutRequest("FakeAuthToken")));

    }
}
