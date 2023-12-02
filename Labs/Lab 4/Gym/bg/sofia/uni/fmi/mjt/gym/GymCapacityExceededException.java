package bg.sofia.uni.fmi.mjt.gym;

public class GymCapacityExceededException extends Exception {
    public GymCapacityExceededException(String message) {
        super(message);
    }

    public GymCapacityExceededException(String message, Throwable clause) {
        super(message, clause);
    }
}
