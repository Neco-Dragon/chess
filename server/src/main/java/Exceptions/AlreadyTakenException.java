package Exceptions;

public class AlreadyTakenException extends ServerException{

    public AlreadyTakenException(String message) {
        super(message);
    }

    public AlreadyTakenException() {
        super("{ \"message\": \"Error: already taken\" }");
    }
    @Override
    public int statusCode() {
        return 403;
    }
}
