package dataAccess;

import model.UserData;

public interface UserDAO {

    /**Clears all user data.
     * @throws DataAccessException if an error occurs during data access
     */
    void clear() throws DataAccessException;

    /**
     * Creates a new user with provided user data.
     *
     * @param userData the user data to create
     * @return the Auth token (with other data) associated with the created user
     * @throws DataAccessException if an error occurs during data access
     */
    UserData createUser(UserData userData) throws DataAccessException;

    /**Retrieves user data based on the username.
     * @param username the username of the user to get
     * @return the retrieved user data
     * @throws DataAccessException if an error occurs during data access
     * @throws BadRequestException if the user is not in the Database
     */
    UserData getUser(String username) throws DataAccessException, BadRequestException, UnauthorizedException;

    /**Retrieves user data based on the password.
     * @param password the password of the user to get
     * @return the retrieved user data
     * @throws DataAccessException if an error occurs during data access
     */
    UserData getPassword(String username, String password) throws DataAccessException, BadRequestException, UnauthorizedException;
}
