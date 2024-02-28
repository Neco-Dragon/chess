package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Random;

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
    public AuthData insertAuth(AuthData authData) throws DataAccessException {
        if (fakeAuthTokenDatabase.get(authData.authToken()) != null){
            throw new DataAccessException();
        }
        fakeAuthTokenDatabase.put(authData.authToken(), authData);
        return authData;
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException, BadRequestException {
        if (fakeAuthTokenDatabase.get(authData.authToken()) == null){
            throw new BadRequestException();
        }
        return authData;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException, BadRequestException {
        if (fakeAuthTokenDatabase.get(authToken) == null){
            throw new BadRequestException();
        }
        fakeAuthTokenDatabase.remove(authToken);
    }

    @Override
    public void confirmAuth(String authToken) throws DataAccessException, UnauthorizedException {
        if (fakeAuthTokenDatabase.get(authToken) == null){
            throw new UnauthorizedException();
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException, BadRequestException, UnauthorizedException {
        confirmAuth(authToken);
        return fakeAuthTokenDatabase.get(authToken).username();
    }


    public String generateAuthToken(String username){
        //"Now that's what I call hashing"
        Random random = new Random();
        return Integer.toHexString(random.nextInt());
    }
}
