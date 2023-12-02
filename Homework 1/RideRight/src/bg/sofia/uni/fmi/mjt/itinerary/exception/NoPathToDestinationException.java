package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class NoPathToDestinationException extends Exception {

    public NoPathToDestinationException(String message) {
        super(message);
    }

    public NoPathToDestinationException(String message, Throwable clause) {
        super(message, clause);
    }
}
