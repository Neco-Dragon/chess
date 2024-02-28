package dataAccess;

public class AlreadyTakenException extends ServerException{
    public AlreadyTakenException(String message) {
        super(message);
    }
    @Override
    public int statusCode() {
        return 403;
    }
}
