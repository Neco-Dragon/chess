package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessUnauthorizedException extends Exception{
    public DataAccessUnauthorizedException(String message) {
        super(message);
    }
}
