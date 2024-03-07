package dataAccess;

import Exceptions.AlreadyTakenException;
import Exceptions.BadRequestException;
import Exceptions.DataAccessException;
import Exceptions.UnauthorizedException;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    /**
     * Key is the username (a String) and the value is the UserData
     * */
    HashMap<String, UserData> fakeUserInfoDatabase = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        fakeUserInfoDatabase.clear();
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException, BadRequestException, AlreadyTakenException {
        fakeUserInfoDatabase.put(userData.username(), userData);
        return userData;
    }

    @Override
    public UserData getUserData(String username) throws UnauthorizedException, BadRequestException, DataAccessException {
        UserData myUserData = fakeUserInfoDatabase.get(username);
        if (myUserData == null){
            throw new DataAccessException();
        }
        return myUserData;
    }

    @Override
    public String getPassword(String username) throws DataAccessException, BadRequestException, UnauthorizedException {
        return getUserData(username).password();
    }
}
