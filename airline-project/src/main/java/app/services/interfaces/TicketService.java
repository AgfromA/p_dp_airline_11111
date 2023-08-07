package app.services.interfaces;

import app.entities.Ticket;
import org.springframework.data.domain.Page;

public interface TicketService {

    Page<Ticket> getAllTickets(int page, int size);

    Ticket getTicketByTicketNumber(String bookingNumber);

    void deleteTicketById(Long id);

    Ticket saveTicket(Ticket ticket);

    Ticket updateTicketById(Long id, Ticket updatedTicket);


    long [] getArrayOfFlightSeatIdByPassengerId(long passengerId);

    void deleteTicketByPassengerId(long passengerId);
}
