package app.exceptions;

import org.springframework.http.HttpStatus;

public class BookedFlightSeatException extends BusinessException {

    public BookedFlightSeatException(Long flightSeatId) {
        super("FlightSeat " + flightSeatId + " is already booked", HttpStatus.BAD_REQUEST);
    }
}