package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataNotFoundException extends Exception{
    public DataNotFoundException(String message) {
        super(message);
    }
}
