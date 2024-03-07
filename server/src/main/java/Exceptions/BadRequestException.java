package Exceptions;

public class BadRequestException extends ServerException{
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException() {
        super("{ \"message\": \"Error: bad request\" }");
    }
    @Override
    public int statusCode() {
        return 400;
    }
}
