package Exceptions;

public class UnauthorizedException extends ServerException{
    public UnauthorizedException(String message) {
        super(message);
    }
    public UnauthorizedException() {
        super("{ \"message\": \"Error: unauthorized\" }");
    }

    @Override
    public int statusCode() {
        return 401;
    }
}