package dataAccess;

import Exceptions.*;
import model.AuthData;

public interface AuthDAO {

    /**Clears all Auth data.
     * @throws DataAccessException if an error occurs during data access
     */
    void clear() throws DataAccessException;

    /**Creates a new Auth record.
     * @param authData the Auth data to create
     * @return the created Auth data
     * @throws DataAccessException if an error occurs during data access
     */
    AuthData insertAuth(AuthData authData) throws DataAccessException, AlreadyTakenException;

    /**
     * Gets Auth data based on provided Auth information.
     *
     * @param authToken the Auth data to get
     * @return the retrieved Auth data
     * @throws DataAccessException if an error occurs during data access
     * @throws BadRequestException if the Auth token is not found
     */
    AuthData getAuth(String authToken) throws DataAccessException, BadRequestException;

    /**Deletes Auth data.
     * @param authToken the Auth data to delete
     * @throws DataAccessException if an error occurs during data access
     */
    void deleteAuth(String authToken) throws DataAccessException, BadRequestException, UnauthorizedException;
    String getUsername(String authToken) throws DataAccessException, BadRequestException, UnauthorizedException;
    String generateAuthToken(String username);

}
