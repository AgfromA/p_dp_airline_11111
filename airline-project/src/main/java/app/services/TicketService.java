package app.services;

import app.dto.TicketDto;
import app.entities.Ticket;

import app.exceptions.EntityNotFoundException;
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
    public Ticket saveTicket(TicketDto timezoneDto) {
        if (passengerService.getPassenger(timezoneDto.getPassengerId()).isEmpty()) {
            throw new EntityNotFoundException("Operation was not finished because Passenger was not found with id = "
                    + timezoneDto.getPassengerId());
        }
        if (flightSeatService.getFlightSeat(timezoneDto.getFlightSeatId()).isEmpty()) {
            throw new EntityNotFoundException("Operation was not finished because FlightSeat was not found with id = "
                    + timezoneDto.getFlightSeatId());
        }
        var ticket = ticketMapper.toEntity(timezoneDto, passengerService, flightService, flightSeatService);
        ticket.setPassenger(passengerRepository.findByEmail(ticket.getPassenger().getEmail()));
        ticket.setFlightSeat(flightSeatRepository
                .findFlightSeatByFlightAndSeat(
                        ticket.getFlightSeat().getFlight().getCode(),
                        ticket.getFlightSeat().getSeat().getSeatNumber()
                ).orElse(null));
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket updateTicketById(Long id, TicketDto timezoneDto) {
        var updatedTicket = ticketMapper.toEntity(timezoneDto, passengerService, flightService, flightSeatService);
        updatedTicket.setId(id);
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

    public List<Ticket> findByFlightSeatId(Long id) {
        return ticketRepository.findByFlightSeatId(id);
    }
}