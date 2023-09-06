package app.exceptions;

public class FlightSeatIsBookedException extends RuntimeException {
    public FlightSeatIsBookedException(String message) {
        super(message);
    }
}
