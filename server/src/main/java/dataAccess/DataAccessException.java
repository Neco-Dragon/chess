package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends ServerException{
    public DataAccessException() {
        super("{ \"message\": \"Error: description\" }");
    }

    @Override
    public int statusCode() {
        return 500;
    }
}
