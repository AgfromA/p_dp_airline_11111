package app.exceptions.handlers;

import app.exceptions.BookedFlightSeatException;
import app.exceptions.EntityNotFoundException;
import app.exceptions.SearchControllerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(SearchControllerException.class)
    public ResponseEntity<String> handleSearchException(SearchControllerException e) {
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
}