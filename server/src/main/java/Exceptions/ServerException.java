package Exceptions;

public abstract class ServerException extends Exception{
    public ServerException(String message) {
        super(message);
    }
    public abstract int statusCode();
}
