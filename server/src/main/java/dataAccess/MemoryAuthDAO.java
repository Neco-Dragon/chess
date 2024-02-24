package dataAccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    /**
     * Key is the authToken (a String) and the value is the AuthData
     * */
    HashMap<String, AuthData> fakeAuthTokenDatabase = new HashMap<>();
    @Override
    public void clear() throws DataAccessException {
        fakeAuthTokenDatabase.clear();
    }

    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        if (fakeAuthTokenDatabase.get(authData.authToken()) != null){
            throw new DataAccessException("Auth Token already exists");
        }
        fakeAuthTokenDatabase.put(authData.authToken(), authData);
        return authData;
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException, DataNotFoundException {
        if (fakeAuthTokenDatabase.get(authData.authToken()) == null){
            throw new DataNotFoundException("No such Auth token exists");
        }
        return authData;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException, DataNotFoundException {
        if (fakeAuthTokenDatabase.get(authData.authToken()) == null){
            throw new DataNotFoundException("No such Auth token exists");
        }
        fakeAuthTokenDatabase.remove(authData.authToken());
    }

    @Override
    public void confirmAuth(AuthData authData) throws DataAccessException, DataAccessUnauthorizedException {
        if (fakeAuthTokenDatabase.get(authData.authToken()) == null){
            throw new DataAccessUnauthorizedException("auth token not found in database");
        }
    }
}
