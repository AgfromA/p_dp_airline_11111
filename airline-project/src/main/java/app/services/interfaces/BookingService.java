package app.services.interfaces;

import app.dto.BookingDTO;
import app.entities.Booking;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    List<BookingDTO> findAll();

    BookingDTO saveBooking(BookingDTO bookingDTO);

    Page<BookingDTO> getAllBookings(Integer page, Integer size);

    BookingDTO getBookingById(Long id);

    List<Booking> getAllBookingsForEmailNotification(LocalDateTime departureIn, LocalDateTime gap);

    void deleteBookingById(Long id);

    BookingDTO getBookingByNumber(String number);

    void deleteBookingByPassengerId(long passengerId);

    void updateBookingAndFlightSeatStatusIfExpired();
}
