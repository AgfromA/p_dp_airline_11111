package app.exceptions;

import app.entities.Ticket;

public class TicketNumberException extends RuntimeException{

    public TicketNumberException(Ticket ticket) {
        super("Ticket number " + ticket.getTicketNumber() + " is already exists");
    }
}
