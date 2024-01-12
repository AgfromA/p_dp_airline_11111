package app.services.interfaces;

import app.dto.BookingDto;
import app.entities.Booking;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    List<BookingDto> findAll();

    BookingDto saveBooking(BookingDto bookingDTO);

    Page<BookingDto> getAllBookings(Integer page, Integer size);

    BookingDto getBookingById(Long id);

    List<Booking> getAllBookingsForEmailNotification(LocalDateTime departureIn, LocalDateTime gap);

    void deleteBookingById(Long id);

    BookingDto getBookingByNumber(String number);

    void deleteBookingByPassengerId(long passengerId);

    void updateBookingAndFlightSeatStatusIfExpired();
}
