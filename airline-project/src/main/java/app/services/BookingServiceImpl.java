package app.services;

import app.entities.Booking;
import app.repositories.BookingRepository;
import app.services.interfaces.BookingService;
import app.services.interfaces.CategoryService;
import app.services.interfaces.FlightService;
import app.services.interfaces.PassengerService;
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
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CategoryService categoryService;
    private final PassengerService passengerService;
    private final FlightService flightService;


    @Transactional
    @Override
    public Booking saveBooking(Booking booking) {
        booking.setPassenger((passengerService.getPassengerById(booking.getPassenger().getId())).get());
        booking.setFlight(flightService.getFlightByCode(booking.getFlight().getCode()));
        booking.setCategory(categoryService.getCategoryByType(booking.getCategory().getCategoryType()));
        if (booking.getId() == 0) {
            booking.setBookingNumber(generateBookingNumber());
        } else {
            booking.setBookingNumber(bookingRepository.findById(booking.getId()).get().getBookingNumber());
        }

        return bookingRepository.save(booking);
    }

    @Override
    public Page<Booking> getAllBookings(Integer page, Integer size) {
        return bookingRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void deleteBookingById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Booking> getAllBookingsForEmailNotification(LocalDateTime departureIn, LocalDateTime gap) {
        return bookingRepository.getAllBooksForEmailNotification(departureIn, gap);
    }

    @Override
    public Booking getBookingByNumber(String number) {
        return bookingRepository.findByBookingNumber(number).orElse(null);
    }

    @Override
    @Transactional
    public void deleteBookingByPassengerId(long passengerId) {
        bookingRepository.deleteBookingByPassengerId(passengerId);
    }

    private String generateBookingNumber() {
        return UUID.randomUUID().toString().substring(0, 9);
    }
}
