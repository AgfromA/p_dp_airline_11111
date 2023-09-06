package app.mappers;

import app.dto.BookingDTO;
import app.entities.*;
import app.enums.BookingStatusType;
import app.services.interfaces.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;


class BookingMapperTest {

    BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    @Mock
    private PassengerService passengerServiceMock = Mockito.mock(PassengerService.class);

    @Mock
    private BookingStatusService bookingStatusServiceMock = Mockito.mock(BookingStatusService.class);

    @Mock
    private FlightSeatService flightSeatServiceMock = Mockito.mock(FlightSeatService.class);

    @Test
    void shouldConvertBookingToBookingDTOEntity() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        when(passengerServiceMock.getPassengerById(1001L)).thenReturn(Optional.of(passenger));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(2L);

        LocalDateTime createTime = LocalDateTime.MIN;

        BookingStatus bookingStatus = new BookingStatus();

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingNumber("BK-111111");
        booking.setBookingDate(LocalDateTime.now());
        booking.setPassenger(passengerServiceMock.getPassengerById(1001L).get());
        booking.setFlightSeat(flightSeat);
        booking.setCreateTime(createTime);
        booking.setStatus(bookingStatus);

        BookingDTO bookingDTO = bookingMapper.convertToBookingDTOEntity(booking);

        Assertions.assertNotNull(bookingDTO);
        Assertions.assertEquals(booking.getId(), bookingDTO.getId());
        Assertions.assertEquals(booking.getBookingNumber(), bookingDTO.getBookingNumber());
        Assertions.assertEquals(booking.getBookingDate(), bookingDTO.getBookingDate());
        Assertions.assertEquals(booking.getPassenger().getId(), bookingDTO.getPassengerId());
        Assertions.assertEquals(booking.getFlightSeat().getId(), bookingDTO.getFlightSeatId());
        Assertions.assertEquals(booking.getCreateTime(), bookingDTO.getCreateTime());
        Assertions.assertEquals(booking.getStatus().getBookingStatusType(), bookingDTO.getBookingStatusType());

    }

    @Test
    void shouldConvertBookingDTOToBookingEntity() throws Exception {

        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        when(passengerServiceMock.getPassengerById(1001L)).thenReturn(Optional.of(passenger));

        LocalDateTime createTime = LocalDateTime.MIN;

        FlightSeat flightSeat = new FlightSeat();
        Long flightSeatId = 2L;
        flightSeat.setId(flightSeatId);

        BookingStatus bookingStatus = new BookingStatus();
        bookingStatus.setBookingStatusType(BookingStatusType.NOT_PAID);


        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setBookingNumber("BK-111111");
        bookingDTO.setBookingDate(LocalDateTime.now());
        bookingDTO.setPassengerId(passengerServiceMock.getPassengerById(1001L).get().getId());
        bookingDTO.setCreateTime(createTime);
        bookingDTO.setFlightSeatId(flightSeatId);
        bookingDTO.setBookingStatusType(BookingStatusType.NOT_PAID);


        when(bookingStatusServiceMock.getBookingStatusByType(BookingStatusType.NOT_PAID)).thenReturn(bookingStatus);
        when(flightSeatServiceMock.getFlightSeatById(flightSeatId)).thenReturn(Optional.of(flightSeat));

        Booking booking = bookingMapper.convertToBookingEntity(bookingDTO, passengerServiceMock, flightSeatServiceMock,
                bookingStatusServiceMock);

        Assertions.assertNotNull(booking);
        Assertions.assertEquals(bookingDTO.getId(), booking.getId());
        Assertions.assertEquals(bookingDTO.getBookingNumber(), booking.getBookingNumber());
        Assertions.assertEquals(bookingDTO.getBookingDate(), booking.getBookingDate());
        Assertions.assertEquals(bookingDTO.getPassengerId(), booking.getPassenger().getId());
        Assertions.assertEquals(bookingDTO.getCreateTime(), booking.getCreateTime());
        Assertions.assertEquals(bookingDTO.getBookingStatusType(), booking.getStatus().getBookingStatusType());
        Assertions.assertEquals(bookingDTO.getFlightSeatId(), booking.getFlightSeat().getId());


    }
}
