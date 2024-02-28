package dataAccess;

public class UnauthorizedException extends ServerException{
    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public int statusCode() {
        return 401;
    }
}