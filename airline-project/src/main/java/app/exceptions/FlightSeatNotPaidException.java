package app.exceptions;

public class FlightSeatNotPaidException extends RuntimeException {
    public FlightSeatNotPaidException() {
        super("The specified flightSeat has not been paid for.");
    }
}