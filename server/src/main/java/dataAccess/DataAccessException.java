package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends ServerException{
    public DataAccessException(String message) {
        super(message);
    }

    @Override
    public int statusCode() {
        return 500;
    }
}
