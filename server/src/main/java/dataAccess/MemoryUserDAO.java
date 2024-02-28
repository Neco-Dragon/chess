package dataAccess;

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
    public UserData createUser(UserData userData) throws DataAccessException {
        if (fakeUserInfoDatabase.get(userData.username()) != null){
            throw new DataAccessException("Username already taken");
        }
        fakeUserInfoDatabase.put(userData.username(), userData);
        return userData;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException, BadRequestException {
        UserData myUserData = fakeUserInfoDatabase.get(username);
        if (myUserData == null){
            throw new BadRequestException("No such username exists");
        }
        return myUserData;
    }

    @Override
    public UserData getPassword(String username, String password) throws DataAccessException, BadRequestException {
        UserData myUserData = fakeUserInfoDatabase.get(username);
        if (myUserData == null){
            throw new BadRequestException("No such username exists");
        }
        if (!Objects.equals(myUserData.password(), password)){
            throw new DataAccessException("Username and password do not match");
        }
        return myUserData;
    }
}
