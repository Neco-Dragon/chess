package ServerFacade;

public class FacadeException extends Exception {
    private final String message;
    public FacadeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
