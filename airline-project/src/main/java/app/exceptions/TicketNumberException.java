package app.exceptions;

import app.entities.Ticket;
import org.springframework.http.HttpStatus;

public class TicketNumberException extends BusinessException{

    public TicketNumberException(Ticket ticket) {
        super("Ticket number " + ticket.getTicketNumber() + " is already exists", HttpStatus.BAD_REQUEST);
    }
}
