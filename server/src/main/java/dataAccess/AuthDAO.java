package dataAccess;

import Exceptions.AlreadyTakenException;
import Exceptions.BadRequestException;
import Exceptions.DataAccessException;
import Exceptions.UnauthorizedException;
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

    /**Deletes Auth data.
     * @param authToken the authToken to delete
     * @throws DataAccessException if an error occurs during data access
     */
    void deleteAuth(String authToken) throws DataAccessException, BadRequestException, UnauthorizedException;

    /**Confirms Auth data.
     * @param authToken the authToken to confirm
     * @throws DataAccessException if an error occurs during data access
     */
    void confirmAuth(String authToken) throws DataAccessException, UnauthorizedException;
    String getUsername(String authToken) throws DataAccessException, BadRequestException, UnauthorizedException;
    String generateAuthToken(String username);

}
