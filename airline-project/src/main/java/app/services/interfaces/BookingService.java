package app.services.interfaces;

import app.dto.BookingDTO;
import app.entities.Booking;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking saveBooking(BookingDTO bookingDTO);

    Page<BookingDTO> getAllBookings(Integer page, Integer size);

    Booking getBookingById(Long id);

    List<Booking> getAllBookingsForEmailNotification(LocalDateTime departureIn, LocalDateTime gap);

    void deleteBookingById(Long id);

    Booking getBookingByNumber(String number);

    void deleteBookingByPassengerId(long passengerId);
}
