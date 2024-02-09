package app.services;

import app.dto.TicketDto;
import app.entities.Booking;
import app.entities.Ticket;

import app.enums.BookingStatus;
import app.exceptions.DuplicateFieldException;
import app.exceptions.EntityNotFoundException;
import app.exceptions.FlightSeatNotPaidException;
import app.exceptions.TicketNumberException;
import app.exceptions.UnPaidBookingException;
import app.mappers.TicketMapper;
import app.repositories.BookingRepository;
import app.repositories.FlightSeatRepository;
import app.repositories.PassengerRepository;
import app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

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
        // Проверяем, оплачено ли место на рейсе
        var bookingCheck = bookingService.getBookingByFlightSeatId(timezoneDto.getFlightSeatId());
        if (bookingCheck.isEmpty() || bookingCheck.get().getBookingStatus() != BookingStatus.PAID) {
            throw new FlightSeatNotPaidException(timezoneDto.getFlightSeatId());
        }
        // Преобразуем DTO в сущность билета
        var ticket = ticketMapper.toEntity(timezoneDto, passengerService, flightService, flightSeatService, bookingService);

        // Находим пассажира по электронной почте и устанавливаем его в билет
        var passenger = passengerRepository.findByEmail(ticket.getPassenger().getEmail());
        ticket.setPassenger(passenger);

        // Находим место на рейсе по идентификатору рейса и номеру места и устанавливаем его в билет
        ticket.setFlightSeat(flightSeatRepository
                .findFirstFlightSeatByFlightIdAndSeat(
                        ticket.getFlightSeat().getFlight().getId(),
                        ticket.getFlightSeat().getSeat().getSeatNumber()).orElse(null));

        // Генерируем номер билета
        String generatedTicketNumber = generateTicketNumber();

        // Проверяем, существует ли билет с указанным номером
        if (ticketRepository.existsByTicketNumber(ticket.getTicketNumber())) {
            throw new TicketNumberException(ticket);
        } else {
            ticket.setTicketNumber(generatedTicketNumber);
        }

        // Проверяем, существует ли уже билет с указанным идентификатором бронирования
        var existingTicket = ticketRepository.findByBookingId(timezoneDto.getBookingId());
        if (existingTicket.isPresent()) {
            throw new DuplicateFieldException("Ticket with bookingId " + timezoneDto.getBookingId() + " already exists!");
        }

        // Устанавливаем бронирование в билет
        if (ticket.getFlightSeat() != null) {
            var flightSeatId = ticket.getFlightSeat().getId();
            if (flightSeatId != null) {
                var booking = bookingRepository.findByFlightSeat(ticket.getFlightSeat());
                ticket.setBooking(booking.orElse(null));
            }
        }

        ticket.setId(null);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket createPaidTicket(Long bookingId) {

        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking with ID " + bookingId + " not found"));

        var ticket = new Ticket();

        if (booking.getBookingStatus() != BookingStatus.PAID) {
            throw new UnPaidBookingException(bookingId);
        } else {
            ticket.setBooking(booking);
            ticket.setPassenger(booking.getPassenger());
            ticket.setFlightSeat(booking.getFlightSeat());
            ticket.setTicketNumber(generateTicketNumber());
        }
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