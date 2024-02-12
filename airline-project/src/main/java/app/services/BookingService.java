package app.services;

import app.dto.BookingDto;
import app.dto.FlightSeatDto;
import app.entities.Booking;
import app.entities.FlightSeat;
import app.enums.BookingStatus;
import app.exceptions.BookedFlightSeatException;
import app.exceptions.EntityNotFoundException;
import app.exceptions.SoldFlightSeatException;
import app.mappers.BookingMapper;
import app.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightSeatService flightSeatService;
    private final BookingMapper bookingMapper;
    private final PassengerService passengerService;

    public List<BookingDto> getAllBookings() {
        return bookingMapper.toDtoList(bookingRepository.findAll());
    }

    public Page<BookingDto> getAllBookings(Integer page, Integer size) {
        return bookingRepository.findAll(PageRequest.of(page, size)).map(bookingMapper::toDto);
    }

    public Optional<BookingDto> getBookingDto(Long id) {
        return bookingRepository.findById(id).map(bookingMapper::toDto);
    }

    @Transactional
    public BookingDto saveBooking(BookingDto bookingDto) {
        bookingDto.setBookingStatus(BookingStatus.NOT_PAID);

        passengerService.checkIfPassengerExists(bookingDto.getPassengerId());
        flightSeatService.checkIfFlightSeatExist(bookingDto.getFlightSeatId());

        var booking = bookingMapper.toEntity(bookingDto, passengerService, flightSeatService);

        checkIfFlightSeatAvailable(booking.getFlightSeat());

        booking.setBookingDate(LocalDateTime.now());
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        var booking = checkIfBookingExist(id);

        // TODO После реализации платежей запретить управление статусом бронирования
        if (bookingDto.getBookingStatus() != null && !bookingDto.getBookingStatus().equals(booking.getBookingStatus())) {
            booking.setBookingStatus(bookingDto.getBookingStatus());
        }
        if (bookingDto.getFlightSeatId() != null && !bookingDto.getFlightSeatId().equals(booking.getFlightSeat().getId())) {
            unbookFlightSeat(booking.getFlightSeat());

            var newFlightSeat = flightSeatService.checkIfFlightSeatExist(bookingDto.getFlightSeatId());
            checkIfFlightSeatAvailable(newFlightSeat);
            booking.setFlightSeat(newFlightSeat);
        }
        if (bookingDto.getPassengerId() != null && !bookingDto.getPassengerId().equals(booking.getPassenger().getId())) {
            var passenger = passengerService.checkIfPassengerExists(bookingDto.getPassengerId());
            booking.setPassenger(passenger);
        }
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Transactional
    public void deleteBookingById(Long id) {
        Booking booking = checkIfBookingExist(id);
        unbookFlightSeat(booking.getFlightSeat());
        bookingRepository.deleteById(id);
    }

    @Transactional
    public void updateBookingAndFlightSeatStatusIfExpired() {
        bookingRepository.findByBookingStatusAndBookingDate(
                BookingStatus.NOT_PAID, LocalDateTime.now().minusMinutes(10)
        ).forEach(booking -> {
            booking.setBookingStatus(BookingStatus.OVERDUE);
            booking.getFlightSeat().setIsBooked(false);
            bookingRepository.save(booking);
        });
    }

    private void checkIfFlightSeatAvailable(FlightSeat flightSeat) {
        if (Boolean.TRUE.equals(flightSeat.getIsSold())) {
            throw new SoldFlightSeatException(flightSeat.getId());
        }
        if (Boolean.TRUE.equals(flightSeat.getIsBooked())) {
            throw new BookedFlightSeatException(flightSeat.getId());
        }
        flightSeat.setIsBooked(true);
    }

    private Booking checkIfBookingExist(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(
                () -> new EntityNotFoundException("Booking with ID: " + bookingId + " not found"));
    }

    private void unbookFlightSeat(FlightSeat flightSeat){
        var flightSeatDto = new FlightSeatDto();
        flightSeatDto.setIsBooked(false);

        flightSeatService.editFlightSeat(flightSeat.getId(), flightSeatDto);
    }
}