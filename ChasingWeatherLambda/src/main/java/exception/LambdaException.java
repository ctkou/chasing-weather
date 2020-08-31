package exception;

public abstract class LambdaException extends RuntimeException {

    public LambdaException(String message) {
        super(message);
    }

    public String getErrorMessageJsonString() {
        return "{\"message\":\"" + this.getMessage() + "\"}";
    }
}
