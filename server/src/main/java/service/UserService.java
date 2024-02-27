package service;
import RequestClasses.LoginRequest;
import RequestClasses.LogoutRequest;
import RequestClasses.RegisterRequest;
import ResultClasses.LoginResult;
import ResultClasses.RegisterResult;
import dataAccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    AuthDAO authDAO;
    UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        UserData myUserData = new UserData(request.username(), request.password(), request.email());
        AuthData myAuthData = new AuthData(authDAO.generateAuthToken(myUserData.username()), myUserData.username());
        userDAO.createUser(myUserData);
        authDAO.insertAuth(myAuthData);
        return new RegisterResult(myUserData.username(), myAuthData.authToken());
    }

    public void logout(LogoutRequest request) throws DataAccessException, DataNotFoundException {
        authDAO.deleteAuth(request.authToken());
    }

    public LoginResult login(LoginRequest request) throws DataNotFoundException, DataAccessException {
        userDAO.getUser(request.username());
        userDAO.getPassword(request.username(), request.password());
        AuthData myAuthData = new AuthData(authDAO.generateAuthToken(request.username()), request.username());
        authDAO.insertAuth(myAuthData);
        return new LoginResult(myAuthData.username(), myAuthData.authToken());
    }

}