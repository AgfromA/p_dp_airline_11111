package app.exceptions;

import exceptions.BusinessException;
import org.springframework.http.HttpStatus;

public class FlightSeatNotPaidException extends BusinessException {

    public FlightSeatNotPaidException(Long flightSeatId) {
        super("The specified flightSeat with " + flightSeatId + " has not been paid for", HttpStatus.BAD_REQUEST);
    }
}