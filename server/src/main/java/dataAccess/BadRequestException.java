package dataAccess;

public class BadRequestException extends ServerException{
    public BadRequestException(String message) {
        super(message);
    }
    @Override
    public int statusCode() {
        return 400;
    }
}
