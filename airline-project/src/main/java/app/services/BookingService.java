package app.services;

import app.dto.BookingDto;
import app.entities.Booking;
import app.enums.BookingStatus;
import app.exceptions.FlightSeatIsBookedException;
import app.mappers.BookingMapper;
import app.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerService passengerService;
    private final FlightSeatService flightSeatService;
    private final BookingMapper bookingMapper;

    public List<BookingDto> findAll() {
        return bookingMapper.convertToBookingDtoEntityList(bookingRepository.findAll());
    }

    @Transactional
    public BookingDto saveBooking(BookingDto bookingDto) {
        var booking = bookingMapper
                .convertToBookingEntity(bookingDto, passengerService, flightSeatService);
        if (booking.getFlightSeat().getIsBooked()) {
            throw new FlightSeatIsBookedException("FlightSeat is already booked.");
        } else {
            booking.getFlightSeat().setIsBooked(true);
        }

        if (booking.getCreateTime() == null) {
            booking.setCreateTime(LocalDateTime.now());
        }

        if (booking.getId() == 0) {
            booking.setBookingNumber(generateBookingNumber());
        } else {
            booking.setBookingNumber(bookingRepository.findById(booking.getId()).get().getBookingNumber());
        }

        return bookingMapper.convertToBookingDtoEntity(bookingRepository.save(booking));
    }

    public Page<BookingDto> getAllBookings(Integer page, Integer size) {
        return bookingRepository.findAll(PageRequest.of(page, size)).map(bookingMapper::convertToBookingDtoEntity);
    }

    public BookingDto getBookingById(Long id) {
        return bookingMapper.convertToBookingDtoEntity(bookingRepository.findById(id).orElse(null));
    }

    @Transactional
    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> getAllBookingsForEmailNotification(LocalDateTime departureIn, LocalDateTime gap) {
        return bookingRepository.getAllBooksForEmailNotification(departureIn, gap);
    }

    public BookingDto getBookingByNumber(String number) {
        return bookingMapper.convertToBookingDtoEntity(bookingRepository.findByBookingNumber(number).orElse(null));
    }

    @Transactional
    public void deleteBookingByPassengerId(long passengerId) {
        bookingRepository.deleteBookingByPassengerId(passengerId);
    }

    private String generateBookingNumber() {
        return UUID.randomUUID().toString().substring(0, 9);
    }

    @Transactional
    public void updateBookingAndFlightSeatStatusIfExpired() {
        List<Booking> bookingList = bookingRepository.findByBookingStatusAndCreateTime(BookingStatus.NOT_PAID,
                LocalDateTime.now().minusMinutes(10));

        for (Booking booking : bookingList) {
            booking.setBookingStatus(BookingStatus.OVERDUE);
            booking.getFlightSeat().setIsBooked(false);
            booking.setCreateTime(null);
            bookingRepository.save(booking);
        }
    }
}