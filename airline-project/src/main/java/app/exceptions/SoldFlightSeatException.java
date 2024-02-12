package app.exceptions;

import org.springframework.http.HttpStatus;

public class SoldFlightSeatException extends BusinessException {

    public SoldFlightSeatException(Long flightSeatId) {
        super("FlightSeat with ID: " + flightSeatId + " is already been sold", HttpStatus.BAD_REQUEST);
    }
}