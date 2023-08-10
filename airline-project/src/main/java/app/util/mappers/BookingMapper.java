package app.util.mappers;

import app.dto.BookingDTO;
import app.entities.Booking;
import app.services.interfaces.CategoryService;
import app.services.interfaces.FlightService;
import app.services.interfaces.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Deprecated
@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final FlightService flightService;
    private final CategoryService categoryService;
    private final PassengerService passengerService;

    public Booking convertToBookingEntity(BookingDTO bookingDTO) {
        var booking = new Booking();
        booking.setId(bookingDTO.getId());
        booking.setBookingNumber(bookingDTO.getBookingNumber());
        booking.setBookingData(bookingDTO.getBookingData());
        booking.setPassenger((passengerService.getPassengerById(bookingDTO.getPassengerId()).get()));
        booking.setFlight(flightService.getFlightById(bookingDTO.getFlightId()).get());
        booking.setCategory(categoryService.getCategoryByType(bookingDTO.getCategoryType()));
        return booking;
    }

    public BookingDTO convertToBookingDTOEntity(Booking booking) {
        var bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setBookingNumber(booking.getBookingNumber());
        bookingDTO.setBookingData(booking.getBookingData());
        bookingDTO.setPassengerId((passengerService.getPassengerById(booking.getPassenger().getId()).get()).getId());
        bookingDTO.setFlightId(flightService.getFlightById(booking.getFlight().getId()).get().getId());
        bookingDTO.setCategoryType(categoryService.getCategoryByType(booking.getCategory().getCategoryType()).getCategoryType());

        return bookingDTO;
    }
}
