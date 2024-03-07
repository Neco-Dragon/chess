package dataAccess;

import Exceptions.*;

import model.AuthData;

public class MySQLAuthDAO implements AuthDAO {

    public MySQLAuthDAO() {

    }
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public AuthData insertAuth(AuthData authData) throws DataAccessException, AlreadyTakenException {
        return null;
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException, BadRequestException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException, BadRequestException, UnauthorizedException {

    }

    @Override
    public void confirmAuth(String authToken) throws DataAccessException, UnauthorizedException {

    }

    @Override
    public String getUsername(String authToken) throws DataAccessException, BadRequestException, UnauthorizedException {
        return null;
    }

    @Override
    public String generateAuthToken(String username) {
        return null;
    }

}
