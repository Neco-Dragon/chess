package dataAccess;

public class AlreadyTakenException extends ServerException{
    public AlreadyTakenException() {
        super("{ \"message\": \"Error: already taken\" }");
    }
    @Override
    public int statusCode() {
        return 403;
    }
}
