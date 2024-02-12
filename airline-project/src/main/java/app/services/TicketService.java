package app.services;

import app.dto.TicketDto;
import app.entities.Ticket;
import app.mappers.TicketMapper;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import app.repositories.PassengerRepository;
import app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final TicketMapper ticketMapper;
    private final PassengerService passengerService;
    private final FlightService flightService;
    private final FlightSeatService flightSeatService;

    public List<TicketDto> getAllTickets() {
        return ticketMapper.toDtoList(ticketRepository.findAll());
    }

    public Page<TicketDto> getAllTickets(int page, int size) {
        return ticketRepository.findAll(PageRequest.of(page, size))
                .map(ticketMapper::toDto);
    }

    public Ticket getTicketByTicketNumber(String ticketNumber) {
        return ticketRepository.findByTicketNumberContainingIgnoreCase(ticketNumber);
    }

    @Transactional
    public void deleteTicketById(Long id) {
        ticketRepository.deleteById(id);
    }

    @Transactional
    public Ticket saveTicket(TicketDto ticketDto) {
        passengerService.checkIfPassengerExists(ticketDto.getPassengerId());
        flightService.checkIfFlightExists(ticketDto.getFlightId());
        flightSeatService.checkIfFlightSeatExist(ticketDto.getFlightSeatId());
        var ticket = ticketMapper.toEntity(ticketDto, passengerService, flightService, flightSeatService);
        ticket.setPassenger(passengerRepository.findByEmail(ticket.getPassenger().getEmail()));
        ticket.setFlight(flightRepository.findByCodeWithLinkedEntities(ticket.getFlight().getCode()));
        ticket.setFlightSeat(flightSeatRepository
                .findFlightSeatByFlightAndSeat(
                        ticket.getFlight().getCode(),
                        ticket.getFlightSeat().getSeat().getSeatNumber()
                ).orElse(null));
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket updateTicketById(Long id, TicketDto ticketDto) {
        var updatedTicket = ticketMapper.toEntity(ticketDto, passengerService, flightService, flightSeatService);
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

    public long[] getFlightSeatIdsByPassengerId(long passengerId) {
        return ticketRepository.findArrayOfFlightSeatIdByPassengerId(passengerId);
    }

    @Transactional
    public void deleteTicketByPassengerId(long passengerId) {
        ticketRepository.deleteTicketByPassengerId(passengerId);
    }

    public List<Ticket> findByFlightId(Long id) {
        return ticketRepository.findByFlightId(id);
    }

    public List<Ticket> getAllTicketsForEmailNotification(LocalDateTime departureIn, LocalDateTime gap) {
        return ticketRepository.getAllTicketsForEmailNotification(departureIn, gap);
    }
}