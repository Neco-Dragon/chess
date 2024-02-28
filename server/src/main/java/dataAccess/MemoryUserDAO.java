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
            throw new DataAccessException();
        }
        fakeUserInfoDatabase.put(userData.username(), userData);
        return userData;
    }

    @Override
    public UserData getUser(String username) throws UnauthorizedException {
        UserData myUserData = fakeUserInfoDatabase.get(username);
        if (myUserData == null){
            throw new UnauthorizedException();
        }
        return myUserData;
    }

    @Override
    public UserData getPassword(String username, String password) throws DataAccessException, BadRequestException {
        UserData myUserData = fakeUserInfoDatabase.get(username);
        if (myUserData == null){
            throw new BadRequestException();
        }
        if (!Objects.equals(myUserData.password(), password)){
            throw new DataAccessException();
        }
        return myUserData;
    }
}
