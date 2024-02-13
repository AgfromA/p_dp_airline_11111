package app.exceptions;

import org.springframework.http.HttpStatus;

public class TicketNumberException extends BusinessException {

    public TicketNumberException(String ticketNumber) {
        super("Ticket number " + ticketNumber + " is already exists", HttpStatus.BAD_REQUEST);
    }
}