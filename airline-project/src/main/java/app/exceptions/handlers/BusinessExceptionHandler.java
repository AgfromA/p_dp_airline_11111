package app.exceptions.handlers;

import app.exceptions.BookedFlightSeatException;
import app.exceptions.EntityNotFoundException;
import app.exceptions.FlightSeatNotPaidException;
import app.exceptions.SearchControllerException;
import app.exceptions.TicketNumberException;
import app.exceptions.UnPaidBookingException;
import org.springframework.http.HttpStatus;
import app.exceptions.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleSearchException(BusinessException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookedFlightSeatException.class)
    public ResponseEntity<String> handleBookedFlightSeatException(BookedFlightSeatException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TicketNumberException.class)
    public ResponseEntity<String> handleTicketNumberException(TicketNumberException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FlightSeatNotPaidException.class)
    public ResponseEntity<String> handleFlightSeatNotPaidException(FlightSeatNotPaidException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnPaidBookingException.class)
    public ResponseEntity<String> handleFlightSeatNotPaidException(UnPaidBookingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}