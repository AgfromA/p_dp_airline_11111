package app.mappers;

import app.dto.BookingDTO;
import app.entities.*;
import app.enums.CategoryType;
import app.services.interfaces.CategoryService;
import app.services.interfaces.FlightService;
import app.services.interfaces.PassengerService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static springfox.documentation.builders.PathSelectors.any;

class BookingMapperTest {

    BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    @Mock
    private PassengerService passengerServiceMock = Mockito.mock(PassengerService.class);
    @Mock
    private FlightService flightServiceMock = Mockito.mock(FlightService.class);
    @Mock
    private CategoryService categoryServiceMock = Mockito.mock(CategoryService.class);

    @Test
    public void shouldConvertBookingToBookingDTOEntity() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
//        passenger.setPassport(new Passport());
//        passenger.setEmail("ema");
//        passenger.setLastName("23");
//        passenger.setFirstName("213");
//        passenger.setBirthDate(LocalDate.of(1920, 2, 26));
//        passenger.setPhoneNumber("213123");
        when(passengerServiceMock.findById(1001L)).thenReturn(1001L);


        Flight flight = new Flight();
        flight.setId(4001L);
        when(flightServiceMock.findById(4001L)).thenReturn(Optional.of(flight));

        Category category = new Category();
        category.setCategoryType(CategoryType.ECONOMY);
        when(categoryServiceMock.findByCategoryType(CategoryType.ECONOMY)).thenReturn(category);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingNumber("BK-111111");
        booking.setBookingData(LocalDateTime.now());
        booking.setPassenger(passengerServiceMock.findById(1001L).get());
        booking.setFlight(flightServiceMock.findById(4001L).get());
        booking.setCategory(categoryServiceMock.findByCategoryType(CategoryType.ECONOMY));

        BookingDTO bookingDTO = bookingMapper.convertToBookingDTOEntity(booking, passengerServiceMock, flightServiceMock,
                categoryServiceMock);

        Assertions.assertEquals(booking.getId(), bookingDTO.getId());
        Assertions.assertEquals(booking.getBookingNumber(), bookingDTO.getBookingNumber());
        Assertions.assertEquals(booking.getBookingData(), bookingDTO.getBookingData());
        Assertions.assertEquals(booking.getPassenger(), bookingDTO.getPassengerId());
        Assertions.assertEquals(booking.getFlight(), bookingDTO.getFlightId());
        Assertions.assertEquals(booking.getCategory(), bookingDTO.getCategoryType());

    }

    @Test
    public void shouldConvertBookingDTOToBookingEntity() throws Exception {

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setBookingNumber("BK-111111");
        bookingDTO.setBookingData(LocalDateTime.now());
        bookingDTO.setPassengerId(passengerServiceMock.findById(1001L).get().getId());
        bookingDTO.setFlightId(flightServiceMock.findById(4001L).get().getId());
        bookingDTO.setCategoryType(CategoryType.ECONOMY);

        Booking booking = bookingMapper.convertToBookingEntity(bookingDTO, passengerServiceMock, flightServiceMock, categoryServiceMock);

        Assertions.assertEquals(bookingDTO.getId(), booking.getId());
        Assertions.assertEquals(bookingDTO.getBookingNumber(), booking.getBookingNumber());
        Assertions.assertEquals(bookingDTO.getBookingData(), booking.getBookingData());
        Assertions.assertEquals(bookingDTO.getPassengerId(), booking.getPassenger().getId());
        Assertions.assertEquals(bookingDTO.getFlightId(), booking.getFlight().getId());
        Assertions.assertEquals(bookingDTO.getCategoryType(), booking.getCategory().getCategoryType());

    }
}
