package app.services;

import app.dto.TicketDTO;
import app.entities.Ticket;

import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import app.repositories.PassengerRepository;
import app.repositories.TicketRepository;
import app.services.interfaces.TicketService;
import app.util.mappers.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final TicketMapper ticketMapper;

    @Override
    public Page<Ticket> getAllTickets(int page, int size) {
        return ticketRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Ticket getTicketByTicketNumber(String ticketNumber) {
        return ticketRepository.findByTicketNumberContainingIgnoreCase(ticketNumber);
    }

    @Override
    @Transactional
    public void deleteTicketById(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Ticket saveTicket(TicketDTO ticketDTO) {
        var ticket = ticketMapper.convertToTicketEntity(ticketDTO);
        ticket.setPassenger(passengerRepository.findByEmail(ticket.getPassenger().getEmail()));
        ticket.setFlight(flightRepository.findByCodeWithLinkedEntities(ticket.getFlight().getCode()));
        ticket.setFlightSeat(flightSeatRepository
                .findFlightSeatByFlightAndSeat(
                ticket.getFlight().getCode(),
                ticket.getFlightSeat().getSeat().getSeatNumber()
                ).orElse(null));
        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Ticket updateTicketById(Long id, TicketDTO updatedTicketDTO) {
        var updatedTicket = ticketMapper.convertToTicketEntity(updatedTicketDTO);
        updatedTicket.setId(id);
        if (updatedTicket.getFlight() == null) {
            updatedTicket.setFlight(ticketRepository.findTicketById(id).getFlight());
        }
        if (updatedTicket.getTicketNumber() == null) {
            updatedTicket.setTicketNumber(ticketRepository.findTicketById(updatedTicket.getId()).getTicketNumber());
        }
        if (updatedTicket.getPassenger() == null) {
            updatedTicket.setPassenger(ticketRepository.findTicketById(id).getPassenger());
        }
        if (updatedTicket.getFlightSeat() == null) {
            updatedTicket.setFlightSeat(ticketRepository.findTicketById(id).getFlightSeat());
        }
        return ticketRepository.save(updatedTicket);
    }

    @Override
    public long [] getArrayOfFlightSeatIdByPassengerId(long passengerId) {
        return ticketRepository.findArrayOfFlightSeatIdByPassengerId(passengerId);
    }
    @Override
    @Transactional
    public void deleteTicketByPassengerId(long passengerId) {
        ticketRepository.deleteTicketByPassengerId(passengerId);
    }

}
