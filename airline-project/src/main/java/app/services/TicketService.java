package app.services;

import app.dto.TicketDto;
import app.entities.Ticket;

import app.enums.BookingStatus;
import app.exceptions.*;
import app.mappers.TicketMapper;
import app.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final PassengerService passengerService;
    private final FlightService flightService;
    private final FlightSeatService flightSeatService;
    private final BookingService bookingService;
    private final Random random = new Random();

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
        checkIfTicketExist(id);
        ticketRepository.deleteById(id);
    }

    @Transactional
    public Ticket saveTicket(TicketDto ticketDto) {

        passengerService.checkIfPassengerExists(ticketDto.getPassengerId());
        flightSeatService.checkIfFlightSeatExist(ticketDto.getFlightSeatId());
        var booking = bookingService.checkIfBookingExist(ticketDto.getBookingId());

        var existingTicket = ticketRepository.findByBookingId(ticketDto.getBookingId());
        if (existingTicket.isPresent()) {
            throw new DuplicateFieldException("Ticket with bookingId " + ticketDto.getBookingId() + " already exists!");
        }
        if (!booking.getFlightSeat().getId().equals(ticketDto.getFlightSeatId())) {
            throw new WrongArgumentException("Ticket should have the same flightSeatId as Booking with bookingId " + ticketDto.getBookingId());
        }
        if (!booking.getPassenger().getId().equals(ticketDto.getPassengerId())) {
            throw new WrongArgumentException("Ticket should have the same passengerId as Booking with bookingId " + ticketDto.getBookingId());
        }
        if (booking.getBookingStatus() != BookingStatus.PAID) {
            throw new FlightSeatNotPaidException(ticketDto.getFlightSeatId());
        }
        if (ticketDto.getTicketNumber() != null && ticketRepository.existsByTicketNumber(ticketDto.getTicketNumber())) {
            throw new TicketNumberException(ticketDto.getTicketNumber());
        } else {
            ticketDto.setTicketNumber(generateTicketNumber());
        }

        var ticket = ticketMapper.toEntity(ticketDto, passengerService, flightService, flightSeatService, bookingService);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket generatePaidTicket(Long bookingId) {
        var existingTicket = ticketRepository.findByBookingId(bookingId);
        if (existingTicket.isPresent()) {
            return existingTicket.get();
        }
        var booking = bookingService.checkIfBookingExist(bookingId);

        if (booking.getBookingStatus() != BookingStatus.PAID) {
            throw new UnPaidBookingException(bookingId);
        } else {
            var ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setPassenger(booking.getPassenger());
            ticket.setFlightSeat(booking.getFlightSeat());
            ticket.setTicketNumber(generateTicketNumber());
            return ticketRepository.save(ticket);
        }
    }

    @Transactional
    public Ticket updateTicketById(Long id, TicketDto ticketDto) {
        var existingTicket = checkIfTicketExist(id);
        var existingBooking = existingTicket.getBooking();

        if (ticketDto.getBookingId() != existingBooking.getId()) {
            throw new WrongArgumentException("Ticket's Booking can't be changed");
        }
        if (ticketDto.getPassengerId() != null
                && !ticketDto.getPassengerId().equals(existingTicket.getPassenger().getId())
                && ticketDto.getPassengerId().equals(existingBooking.getPassenger().getId())) {
            // FIXME надо бы эксепшн выбраисывать, если пришедший айди пассажира не совпадает с айди пассажира у связанного бронирования
            existingTicket.setPassenger(existingBooking.getPassenger());
        }
        if (ticketDto.getFlightSeatId() != null
                && !ticketDto.getFlightSeatId().equals(existingTicket.getFlightSeat().getId())
                && ticketDto.getFlightSeatId().equals(existingBooking.getFlightSeat().getId())) {
            // FIXME надо бы эксепшн выбраисывать, если пришедший айди сиденья не совпадает с айди сиденья у связанного бронирования
            existingTicket.setFlightSeat(existingBooking.getFlightSeat());
        }
        if (ticketDto.getTicketNumber() != null
                && !ticketDto.getTicketNumber().equals(existingTicket.getTicketNumber())
                && !ticketRepository.existsByTicketNumber(ticketDto.getTicketNumber())) {
            existingTicket.setTicketNumber(ticketDto.getTicketNumber());
        }
        return ticketRepository.save(existingTicket);
    }

    public long[] getFlightSeatIdsByPassengerId(long passengerId) {
        return ticketRepository.findArrayOfFlightSeatIdByPassengerId(passengerId);
    }

    @Transactional
    public void deleteTicketByPassengerId(long passengerId) {
        ticketRepository.deleteTicketByPassengerId(passengerId);
    }

    public String generateTicketNumber() {
        StringBuilder ticketNumberBuilder;
        do {
            ticketNumberBuilder = new StringBuilder();

            for (int i = 0; i < 2; i++) {
                char letter = (char) (random.nextInt(26) + 'A');
                ticketNumberBuilder.append(letter);
            }

            ticketNumberBuilder.append("-");

            for (int i = 0; i < 4; i++) {
                int digit = random.nextInt(10);
                ticketNumberBuilder.append(digit);
            }
        } while (ticketRepository.existsByTicketNumber(ticketNumberBuilder.toString()));
        return ticketNumberBuilder.toString();
    }

    public List<Ticket> getAllTicketsForEmailNotification(LocalDateTime departureIn, LocalDateTime gap) {
        return ticketRepository.getAllTicketsForEmailNotification(departureIn, gap);
    }

    public Ticket checkIfTicketExist(Long ticketId) {
        return ticketRepository.findTicketById(ticketId).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because Ticket was not found with id = " + ticketId)
        );
    }
}