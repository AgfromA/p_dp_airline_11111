package app.exceptions;

public class BookedFlightSeatException extends RuntimeException {

    public BookedFlightSeatException(Long flightSeatId) {
        super("FlightSeat " + flightSeatId + " is already booked");
    }
}