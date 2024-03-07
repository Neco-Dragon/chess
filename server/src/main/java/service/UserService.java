package service;
import Exceptions.AlreadyTakenException;
import Exceptions.BadRequestException;
import Exceptions.DataAccessException;
import Exceptions.UnauthorizedException;
import RequestClasses.LoginRequest;
import RequestClasses.LogoutRequest;
import RequestClasses.RegisterRequest;
import ResultClasses.LoginResult;
import ResultClasses.RegisterResult;
import dataAccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {
    AuthDAO authDAO;
    UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public RegisterResult register(RegisterRequest request) throws DataAccessException, BadRequestException, AlreadyTakenException, UnauthorizedException {
        UserData myUserData = new UserData(request.username(), request.password(), request.email());
        AuthData myAuthData = new AuthData(authDAO.generateAuthToken(myUserData.username()), myUserData.username());
        if (myUserData.username() == null){
            throw new BadRequestException();
        }
        if (myUserData.password() == null){
            throw new BadRequestException();
        }
        if (userDAO.getUserData(myUserData.username()) != null){
            throw new AlreadyTakenException();
        }
        userDAO.createUser(myUserData);
        authDAO.insertAuth(myAuthData);
        return new RegisterResult(myUserData.username(), myAuthData.authToken());
    }

    public void logout(LogoutRequest request) throws DataAccessException, BadRequestException, UnauthorizedException {
        authDAO.deleteAuth(request.authToken());
    }

    public LoginResult login(LoginRequest request) throws BadRequestException, DataAccessException, UnauthorizedException, AlreadyTakenException {
        if (request.username() == null || request.password() == null){
            throw new BadRequestException();
        }
        UserData userData = userDAO.getUserData(request.username());
        if (!Objects.equals(request.password(), userData.password())){
            throw new UnauthorizedException();
        }
        userDAO.getUserData(request.username());
        userDAO.getPassword(request.username());
        AuthData myAuthData = new AuthData(authDAO.generateAuthToken(request.username()), request.username());
        authDAO.insertAuth(myAuthData);
        return new LoginResult(myAuthData.username(), myAuthData.authToken());
    }

}