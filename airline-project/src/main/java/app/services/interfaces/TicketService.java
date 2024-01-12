package app.services.interfaces;

import app.dto.TicketDto;
import app.entities.Ticket;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketService {
    List<TicketDto> getAllTickets();

    Page<TicketDto> getAllTickets(int page, int size);

    Ticket getTicketByTicketNumber(String bookingNumber);

    void deleteTicketById(Long id);

    Ticket saveTicket(TicketDto ticketDTO);

    Ticket updateTicketById(Long id, TicketDto ticketDTO);


    long[] getArrayOfFlightSeatIdByPassengerId(long passengerId);

    void deleteTicketByPassengerId(long passengerId);

    List<Ticket> findByFlightId(Long id);
}
