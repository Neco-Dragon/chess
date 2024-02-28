package Exceptions;

public class UnauthorizedException extends ServerException{
    public UnauthorizedException() {
        super("{ \"message\": \"Error: unauthorized\" }");
    }

    @Override
    public int statusCode() {
        return 401;
    }
}