package dataAccess;

public class BadRequestException extends ServerException{
    public BadRequestException() {
        super("{ \"message\": \"Error: bad request\" }");
    }
    @Override
    public int statusCode() {
        return 400;
    }
}
