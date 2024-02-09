package app.services;

import app.dto.TicketDto;
import app.entities.Booking;
import app.entities.FlightSeat;
import app.entities.Passenger;
import app.entities.Ticket;

import app.enums.BookingStatus;
import app.exceptions.EntityNotFoundException;
import app.exceptions.FlightSeatNotPaidException;
import app.exceptions.TicketNumberException;
import app.mappers.TicketMapper;
import app.repositories.BookingRepository;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import app.repositories.PassengerRepository;
import app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
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
        // Check if the booking for the flightSeat is paid
        Optional<Booking> bookingCheck = bookingService.getBookingByFlightSeatId(timezoneDto.getFlightSeatId());
        if (bookingCheck.isEmpty() || bookingCheck.get().getBookingStatus() != BookingStatus.PAID) {
            throw new FlightSeatNotPaidException();
        }

        Optional<Ticket> existingTicket = ticketRepository.findByBookingId(timezoneDto.getBookingId());
        if (existingTicket.isPresent()) {
            ticketMapper.toDto(existingTicket.get());
        }

        var ticket = ticketMapper.toEntity(timezoneDto, passengerService, flightService, flightSeatService, bookingService);

        var passenger = passengerRepository.findByEmail(ticket.getPassenger().getEmail()).orElse(null);

        ticket.setPassenger(passenger);

        ticket.setFlightSeat(flightSeatRepository
                .findFirstFlightSeatByFlightIdAndSeat(
                        ticket.getFlightSeat().getFlight().getId(),
                        ticket.getFlightSeat().getSeat().getSeatNumber()).orElse(null));
        if (ticketRepository.existsByTicketNumber(ticket.getTicketNumber())) {
            throw new TicketNumberException(ticket);
        } else {
            String generatedTicketNumber = generateTicketNumber();
            if (generatedTicketNumber == null) {
                throw new IllegalArgumentException("Ticket number cannot be null");
            }
            ticket.setTicketNumber(generatedTicketNumber);
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
    public Ticket createPaidTicket(Long bookingId)  {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("\"Booking not found with ID: " + bookingId));

        if (booking.getBookingStatus() != BookingStatus.PAID) {
            throw new FlightSeatNotPaidException();
        }

        Ticket ticket = new Ticket();
        ticket.setBooking(booking);
        ticket.setPassenger(booking.getPassenger());
        ticket.setFlightSeat(booking.getFlightSeat());
        ticket.setTicketNumber(generateTicketNumber());

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket updateTicketById(Long id, TicketDto timezoneDto) {
        var updatedTicket = ticketMapper.toEntity(timezoneDto, passengerService, flightService, flightSeatService, bookingService);
        updatedTicket.setId(id);

        // Проверяем, был ли изменен passengerId в ticketDto
        if (timezoneDto.getPassengerId() == null) {
            // Если passengerId не указан, возвращаем текущего passenger
            updatedTicket.setPassenger(ticketRepository.findTicketById(id).getPassenger());
        } else {
            // Проверяем, был ли изменен passengerId в связанном Booking
            Booking booking = ticketRepository.findTicketById(id).getBooking();
            if (booking != null && !booking.getPassenger().getId().equals(timezoneDto.getPassengerId())) {
                throw new IllegalArgumentException("Passenger cannot be changed because it is already assigned to a booking.");
            }
        }

        // Проверяем, был ли изменен flightSeatId в ticketDto
        if (timezoneDto.getFlightSeatId() == null) {
            // Если flightSeatId не указан, возвращаем текущий flightSeat
            updatedTicket.setFlightSeat(ticketRepository.findTicketById(id).getFlightSeat());
        } else {
            // Проверяем, был ли изменен flightSeatId в связанном Booking
            Booking booking = ticketRepository.findTicketById(id).getBooking();
            if (booking != null && !booking.getFlightSeat().getId().equals(timezoneDto.getFlightSeatId())) {
                throw new IllegalArgumentException("FlightSeat cannot be changed because it is already assigned to a booking.");
            }
        }

        // Устанавливаем номер билета, если он не указан
        if (updatedTicket.getTicketNumber() == null) {
            updatedTicket.setTicketNumber(ticketRepository.findTicketById(updatedTicket.getId()).getTicketNumber());
        }

        // Устанавливаем booking, если flightSeat не указан
        if (updatedTicket.getFlightSeat() != null && updatedTicket.getBooking() == null) {
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