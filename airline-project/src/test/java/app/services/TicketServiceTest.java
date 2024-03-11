package app.services;

import app.dto.TicketDto;
import app.entities.Booking;
import app.entities.Destination;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Passenger;
import app.entities.Seat;
import app.entities.Ticket;
import app.enums.Airport;
import app.enums.BookingStatus;
import app.exceptions.EntityNotFoundException;
import app.exceptions.UnPaidBookingException;
import app.mappers.TicketMapper;
import app.repositories.BookingRepository;
import app.repositories.FlightSeatRepository;
import app.repositories.PassengerRepository;
import app.repositories.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private PassengerService passengerService;
    @Mock
    private FlightSeatService flightSeatService;
    @Mock
    private FlightSeatRepository flightSeatRepository;
    @Mock
    private FlightService flightService;
    @Mock
    private BookingService bookingService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    @DisplayName("1 findAllTickets(), Positive test finds 1 ticket")
    @Test
    void shouldReturnOneTicket() {

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Марк");
        passenger.setLastName("Теплицын");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setPassenger(passenger);
        booking.setBookingStatus(BookingStatus.PAID);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        flightSeat.setSeat(seat);

        TicketDto ticket = new TicketDto();
        ticket.setId(1L);
        ticket.setTicketNumber("LL-4000");
        ticket.setBookingId(booking.getId());
        ticket.setPassengerId(passenger.getId());
        ticket.setFlightSeatId(flightSeat.getId());

        List<TicketDto> expectedTickets = new ArrayList<>();
        expectedTickets.add(ticket);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket());
        ticketList.add(new Ticket());

        when(ticketRepository.findAll()).thenReturn(ticketList);

        when(ticketMapper.toDtoList(ticketList)).thenReturn(expectedTickets);

        List<TicketDto> actualTickets = ticketService.getAllTickets();

        assertEquals(1, actualTickets.size());
    }

    @DisplayName("2 findAllTickets(), Positive test finds 2 tickets")
    @Test
    void shouldReturnTwoTickets() {

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Марк");
        passenger.setLastName("Теплицын");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setPassenger(passenger);
        booking.setBookingStatus(BookingStatus.PAID);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        flightSeat.setSeat(seat);

        TicketDto ticket = new TicketDto();
        ticket.setId(1L);
        ticket.setTicketNumber("LL-4000");
        ticket.setBookingId(booking.getId());
        ticket.setPassengerId(passenger.getId());
        ticket.setFlightSeatId(flightSeat.getId());

        Passenger passenger2 = new Passenger();
        passenger2.setId(2L);
        passenger2.setFirstName("Лиза");
        passenger2.setLastName("Теплицына");

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setPassenger(passenger2);
        booking2.setBookingStatus(BookingStatus.PAID);

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setSeatNumber("2A");

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(2L);
        flightSeat2.setSeat(seat2);

        TicketDto ticket2 = new TicketDto();
        ticket2.setId(2L);
        ticket2.setTicketNumber("LL-4001");
        ticket2.setBookingId(booking2.getId());
        ticket2.setPassengerId(passenger2.getId());
        ticket2.setFlightSeatId(flightSeat2.getId());

        List<TicketDto> expectedTickets = new ArrayList<>();
        expectedTickets.add(ticket);
        expectedTickets.add(ticket2);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket());
        ticketList.add(new Ticket());

        when(ticketRepository.findAll()).thenReturn(ticketList);

        when(ticketMapper.toDtoList(ticketList)).thenReturn(expectedTickets);

        List<TicketDto> actualTickets = ticketService.getAllTickets();

        assertEquals(2, actualTickets.size());
    }

    @DisplayName("3 findAllTickets(), Negative test finds 2 tickets")
    @Test
    void shouldReturnAllTickets() {

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Марк");
        passenger.setLastName("Теплицын");

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        flightSeat.setSeat(seat);

        TicketDto ticket = new TicketDto();
        ticket.setId(1L);
        ticket.setTicketNumber("LL-4000");
        ticket.setPassengerId(passenger.getId());
        ticket.setFlightSeatId(flightSeat.getId());

        Passenger passenger2 = new Passenger();
        passenger2.setId(2L);
        passenger2.setFirstName("Лиза");
        passenger2.setLastName("Теплицына");

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setSeatNumber("2A");

        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(2L);
        flightSeat2.setSeat(seat2);

        TicketDto ticket2 = new TicketDto();
        ticket2.setId(2L);
        ticket2.setTicketNumber("LL-4001");
        ticket2.setPassengerId(passenger2.getId());
        ticket2.setFlightSeatId(flightSeat2.getId());

        List<TicketDto> expectedTickets = new ArrayList<>();
        expectedTickets.add(ticket);
        expectedTickets.add(ticket2);

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket());
        ticketList.add(new Ticket());

        when(ticketRepository.findAll()).thenReturn(ticketList);

        when(ticketMapper.toDtoList(ticketList)).thenReturn(new ArrayList<>());

        List<TicketDto> actualTickets = ticketService.getAllTickets();

        assertEquals(0, actualTickets.size());
    }

    @DisplayName("4 findTicketByTicketNumber(), Positive test finds ticket by ticket number")
    @Test
    void shouldReturnOneTicketByTicketNumber() {

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("Марк");
        passenger.setLastName("Теплицын");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setPassenger(passenger);
        booking.setBookingStatus(BookingStatus.PAID);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(1L);
        flightSeat.setSeat(seat);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTicketNumber("LL-4000");
        ticket.setBooking(booking);
        ticket.setPassenger(passenger);
        ticket.setFlightSeat(flightSeat);

        when(ticketRepository.findByTicketNumberContainingIgnoreCase(ticket.getTicketNumber())).thenReturn(Optional.of(ticket));

        var result = ticketService.getTicketByTicketNumber(ticket.getTicketNumber()).get();

        assertEquals(ticket, result);
    }

    @DisplayName("5 deleteTicketById(), Positive test delete ticket by ticket id")
    @Test
    void shouldDeleteTicketById() {
        var ticketId = 1L;
        when(ticketRepository.findTicketById(ticketId)).thenReturn(Optional.of(new Ticket()));
        ticketService.deleteTicketById(ticketId);
        verify(ticketRepository, times(1)).deleteById(ticketId);
    }

    @DisplayName("6 saveTicket(), Positive test save ticket")
    @Test
    void shouldSaveTicket() {

        TicketDto ticketDto = new TicketDto();
        ticketDto.setPassengerId(1L);
        ticketDto.setFlightSeatId(3L);
        ticketDto.setBookingId(1L);

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("test@example.com");
        passenger.setLastName("Теплицын");
        passenger.setFirstName("Марк");
        when(passengerService.checkIfPassengerExists(ticketDto.getPassengerId())).thenReturn(passenger);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(2L);
        toKoltcovo.setAirportCode(Airport.SVX);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setCode("VKOSVX");
        flight.setFrom(fromVnukovo);
        flight.setTo(toKoltcovo);

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3L);
        flightSeat.setFlight(flight);
        flightSeat.setSeat(seat);

        List<FlightSeat> flightSeats = new ArrayList<>();
        flightSeats.add(flightSeat);

        flight.setSeats(flightSeats);

        when(flightSeatService.checkIfFlightSeatExist(ticketDto.getFlightSeatId())).thenReturn(flightSeat);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingStatus(BookingStatus.PAID);
        booking.setPassenger(passenger);
        booking.setFlightSeat(flightSeat);
        when(bookingService.checkIfBookingExist(ticketDto.getBookingId())).thenReturn(booking);

        when(ticketRepository.findByBookingId(ticketDto.getBookingId())).thenReturn(Optional.empty());

        Ticket savedTicket = new Ticket();
        savedTicket.setId(1L);
        savedTicket.setPassenger(passenger);
        savedTicket.setBooking(booking);
        savedTicket.setFlightSeat(flightSeat);
        savedTicket.setTicketNumber("LL-4000");
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);
        when(ticketMapper.toEntity(ticketDto)).thenReturn(savedTicket);

        Ticket result = ticketService.saveTicket(ticketDto);

        assertNotNull(result);
        assertNotNull(result.getTicketNumber());
        assertEquals(result.getTicketNumber(), savedTicket.getTicketNumber());
        assertEquals(result.getPassenger(), savedTicket.getPassenger());
        assertEquals(result.getFlightSeat(), savedTicket.getFlightSeat());
        assertEquals(result.getBooking(), savedTicket.getBooking());
    }

    @DisplayName("7 generatePaidTicket(), Positive test generate paid ticket")
    @Test
    void shouldGeneratePaidTicket() {

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("test@example.com");
        passenger.setLastName("Теплицын");
        passenger.setFirstName("Марк");

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(2L);
        toKoltcovo.setAirportCode(Airport.SVX);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setCode("VKOSVX");
        flight.setFrom(fromVnukovo);
        flight.setTo(toKoltcovo);

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3L);
        flightSeat.setFlight(flight);
        flightSeat.setSeat(seat);

        List<FlightSeat> flightSeats = new ArrayList<>();
        flightSeats.add(flightSeat);

        flight.setSeats(flightSeats);

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.PAID);
        booking.setPassenger(passenger);
        booking.setFlightSeat(flightSeat);
        booking.setId(1L);

        Ticket savedTicket = new Ticket();
        savedTicket.setTicketNumber("LL-4000");
        savedTicket.setPassenger(passenger);
        savedTicket.setFlightSeat(flightSeat);
        savedTicket.setBooking(booking);

        when(bookingService.checkIfBookingExist(1L)).thenReturn(booking);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        Ticket result = ticketService.generatePaidTicket(booking.getId());
        assertNotNull(result);
        assertEquals(booking.getPassenger(), result.getPassenger());
        assertEquals(booking.getFlightSeat(), result.getFlightSeat());
    }

    @DisplayName("8 createPaidTicket(), Negative test does no create ticket, because booking not paid")
    @Test
    void shouldNotCreateUPaidTicket() {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("test@example.com");
        passenger.setLastName("Теплицын");
        passenger.setFirstName("Марк");

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("1A");

        Destination fromVnukovo = new Destination();
        fromVnukovo.setId(1L);
        fromVnukovo.setAirportCode(Airport.VKO);

        Destination toKoltcovo = new Destination();
        toKoltcovo.setId(2L);
        toKoltcovo.setAirportCode(Airport.SVX);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setCode("VKOSVX");
        flight.setFrom(fromVnukovo);
        flight.setTo(toKoltcovo);

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(3L);
        flightSeat.setFlight(flight);
        flightSeat.setSeat(seat);

        List<FlightSeat> flightSeats = new ArrayList<>();
        flightSeats.add(flightSeat);

        flight.setSeats(flightSeats);

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.NOT_PAID);
        booking.setPassenger(passenger);
        booking.setFlightSeat(flightSeat);
        booking.setId(1L);

        Ticket savedTicket = new Ticket();
        savedTicket.setTicketNumber("LL-4000");
        savedTicket.setPassenger(passenger);
        savedTicket.setFlightSeat(flightSeat);
        savedTicket.setBooking(booking);

        when(bookingService.checkIfBookingExist(1L)).thenReturn(booking);

        assertThrows(UnPaidBookingException.class, () -> {
            ticketService.generatePaidTicket(1L);
        });
    }

    @DisplayName("9 createPaidTicket(), Negative test does not create paid ticket, because such booking not exist")
    @Test
    void shouldNotCreateTicketByNonExistentBooking() {
        when(bookingService.checkIfBookingExist(1L)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> ticketService.generatePaidTicket(1L));
    }

    @DisplayName("14 getFlightSeatIdsByPassengerId(), Positive test return an array of flight seat IDs for a valid passenger ID")
    @Test
    void shouldReturnFlightSeatIdByPassengerId() {
        long passengerId = 1;
        long[] expected = {1, 2, 3};
        when(ticketRepository.findArrayOfFlightSeatIdByPassengerId(passengerId)).thenReturn(expected);

        long[] result = ticketService.getFlightSeatIdsByPassengerId(passengerId);

        assertArrayEquals(expected, result);
    }

    @DisplayName("15 getFlightSeatIdsByPassengerId(), Negative test return an empty array for a passenger with no tickets")
    @Test
    void shouldReturnFlightSeatIdByPassengerId2() {
        long passengerId = 1;
        long[] expected = {};
        when(ticketRepository.findArrayOfFlightSeatIdByPassengerId(passengerId)).thenReturn(expected);

        long[] result = ticketService.getFlightSeatIdsByPassengerId(passengerId);

        assertArrayEquals(expected, result);
    }

    @DisplayName("16 deleteTicketByPassengerId(), Positive test delete ticket associated with a passenger ID")
    @Test
    void shouldDeleteTicketByPassengerId() {
        long passengerId = 1;

        ticketService.deleteTicketByPassengerId(passengerId);

        verify(ticketRepository, times(1)).deleteTicketByPassengerId(passengerId);
    }

    @DisplayName("17 generateUniqueTicketNumber(), Positive test generates a unique ticket number")
    @Test
    void shouldGenerateUniqueTicketNumber() {
        String ticketNumber = ticketService.generateTicketNumber();

        String anotherTicketNumber = ticketService.generateTicketNumber();

        assertNotEquals(ticketNumber, anotherTicketNumber);
    }

    @DisplayName("18 generateTicketNumberWithCorrectFormat(), Positive test validates ticket number format")
    @Test
    void shouldGenerateTicketNumberWithCorrectFormat() {

        String ticketNumber = ticketService.generateTicketNumber();

        boolean isValid = ticketNumber.matches("[A-Z]{2}-\\d{4}");

        assertTrue(isValid);
    }
}