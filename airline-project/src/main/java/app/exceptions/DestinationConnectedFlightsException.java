package app.exceptions;

import org.springframework.http.HttpStatus;

public class DestinationConnectedFlightsException extends BusinessException {

    private static final String MESSAGE = "Operation was not finished because Destination %d has connected Flight";

    public DestinationConnectedFlightsException(Long id) {
        super(String.format(MESSAGE, id), HttpStatus.BAD_REQUEST);
    }
}