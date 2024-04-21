package bg.sofia.uni.fmi.mjt.cooking.exception;

public class LimitExceededException extends Exception {

    public LimitExceededException() {
    }

    public LimitExceededException(String message) {
        super(message);
    }

    public LimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }

}
