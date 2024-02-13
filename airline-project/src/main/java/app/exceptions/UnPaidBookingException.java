package app.exceptions;

import org.springframework.http.HttpStatus;

public class UnPaidBookingException extends BusinessException {

    public UnPaidBookingException(Long bookingId) {
        super("Booking with ID " + bookingId + " has not been paid for", HttpStatus.BAD_REQUEST);
    }
}