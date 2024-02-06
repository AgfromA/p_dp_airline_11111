package app.services;

import app.dto.TicketDto;
import app.entities.Booking;
import app.entities.FlightSeat;
import app.entities.Passenger;
import app.entities.Ticket;

import app.exceptions.EntityNotFoundException;
import app.exceptions.TicketNumberException;
import app.mappers.TicketMapper;
import app.repositories.BookingRepository;
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
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final FlightSeatRepository flightSeatRepository;
    private final TicketMapper ticketMapper;
    private final PassengerService passengerService;
    private final FlightService flightService;
    private final FlightSeatService flightSeatService;
    private final BookingService bookingService;

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
        if (bookingService.getBooking(timezoneDto.getBookingId()).isEmpty()) {
            throw new EntityNotFoundException("Operation was not finished because Booking was not found with id = "
                    + timezoneDto.getBookingId());
        }
        var ticket = ticketMapper.toEntity(timezoneDto, passengerService, flightService, flightSeatService, bookingService);
        List<Passenger> passengers = passengerRepository.findByEmail(ticket.getPassenger().getEmail());
        if (passengers.isEmpty()) {
            // Email not found, handle the situation accordingly
        } else if (passengers.size() == 1) {
            Passenger passenger = passengers.get(0);
            ticket.setPassenger(passenger);
        } else {
            // Multiple passengers found with the same email, handle the situation accordingly
        }
        ticket.setFlightSeat(flightSeatRepository
                .findFlightSeatByFlightAndSeat(
                        ticket.getFlightSeat().getFlight().getCode(),
                        ticket.getFlightSeat().getSeat().getSeatNumber()).orElse(null));
        if (ticketRepository.existsByTicketNumber(ticket.getTicketNumber())) {
            throw new TicketNumberException(ticket);
        } else {
            ticket.setTicketNumber(generateTicketNumber());
        }
        if (ticket.getFlightSeat() != null) {
            Long flightSeatId = ticket.getFlightSeat().getId();
            if (flightSeatId != null) {
                Optional<Booking> booking = bookingRepository.findByFlightSeat(ticket.getFlightSeat());
                ticket.setBooking(booking.orElse(null));
            }
        }
        ticket.setId(null);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket updateTicketById(Long id, TicketDto timezoneDto) {
        var updatedTicket = ticketMapper.toEntity(timezoneDto, passengerService, flightService, flightSeatService,bookingService);
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
        if (updatedTicket.getFlightSeat() == null) {
            updatedTicket.setBooking(ticketRepository.findTicketById(id).getBooking());
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


    private String generateTicketNumber() {
        Random random = new Random();
        StringBuilder ticketNumberBuilder = new StringBuilder();

        for (int i = 0; i < 2; i++) {
            char letter = (char) (random.nextInt(26) + 'A');
            ticketNumberBuilder.append(letter);
        }

        ticketNumberBuilder.append("-");

        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10);
            ticketNumberBuilder.append(digit);
        }

        return ticketNumberBuilder.toString();
    }
}