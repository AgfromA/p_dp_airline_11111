package app.mappers;

import app.dto.BookingDto;
import app.entities.*;
import app.enums.BookingStatus;
import app.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;



class BookingMapperTest {

    @InjectMocks
    private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    @Mock
    private PassengerService passengerServiceMock;

    @Mock
    private FlightSeatService flightSeatServiceMock;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldConvertBookingToBookingDTOEntity() {
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        when(passengerServiceMock.getPassenger(1001L)).thenReturn(Optional.of(passenger));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(2L);

        LocalDateTime createTime = LocalDateTime.MIN;

        Booking booking = new Booking();
        booking.setId(1L);

        booking.setBookingDate(LocalDateTime.now());

        booking.setFlightSeat(flightSeat);
        booking.setPassenger(passenger);

        booking.setBookingStatus(BookingStatus.NOT_PAID);

        BookingDto bookingDTO = bookingMapper.toDto(booking);

        Assertions.assertNotNull(bookingDTO);
        Assertions.assertEquals(booking.getId(), bookingDTO.getId());

        Assertions.assertEquals(booking.getBookingDate(), bookingDTO.getBookingDate());

        Assertions.assertEquals(booking.getFlightSeat().getId(), bookingDTO.getFlightSeatId());

        Assertions.assertEquals(booking.getBookingStatus(), bookingDTO.getBookingStatus());

    }



    @Test
    void shouldConvertBookingListToBookingDTOList() throws Exception {
        List<Booking> bookingList = new ArrayList<>();

        Passenger passenger1 = new Passenger();
        passenger1.setId(1001L);
        when(passengerServiceMock.getPassenger(1001L)).thenReturn(Optional.of(passenger1));
        Passenger passenger2 = new Passenger();
        passenger2.setId(1002L);
        when(passengerServiceMock.getPassenger(1002L)).thenReturn(Optional.of(passenger2));

        FlightSeat flightSeat1 = new FlightSeat();
        flightSeat1.setId(2L);
        FlightSeat flightSeat2 = new FlightSeat();
        flightSeat2.setId(4L);

        LocalDateTime createTime = LocalDateTime.MIN;

        Booking bookingOne = new Booking();
        bookingOne.setId(1L);

        bookingOne.setBookingDate(LocalDateTime.now());

        bookingOne.setFlightSeat(flightSeat1);

        bookingOne.setBookingStatus(BookingStatus.NOT_PAID);

        bookingOne.setPassenger(passenger1);

        Booking bookingTwo = new Booking();
        bookingTwo.setId(2L);

        bookingTwo.setBookingDate(LocalDateTime.now());

        bookingTwo.setFlightSeat(flightSeat2);

        bookingTwo.setBookingStatus(BookingStatus.PAID);

        bookingTwo.setPassenger(passenger2);

        bookingList.add(bookingOne);
        bookingList.add(bookingTwo);

        List<BookingDto> bookingDtoList = bookingMapper.toDtoList(bookingList);

        Assertions.assertEquals(bookingList.size(), bookingDtoList.size());

        Assertions.assertEquals(bookingList.get(0).getId(), bookingDtoList.get(0).getId());

        Assertions.assertEquals(bookingList.get(0).getBookingDate(), bookingDtoList.get(0).getBookingDate());

        Assertions.assertEquals(bookingList.get(0).getFlightSeat().getId(), bookingDtoList.get(0).getFlightSeatId());

        Assertions.assertEquals(bookingList.get(0).getBookingStatus(), bookingDtoList.get(0).getBookingStatus());

        Assertions.assertEquals(bookingList.get(1).getId(), bookingDtoList.get(1).getId());

        Assertions.assertEquals(bookingList.get(1).getBookingDate(), bookingDtoList.get(1).getBookingDate());

        Assertions.assertEquals(bookingList.get(1).getFlightSeat().getId(), bookingDtoList.get(1).getFlightSeatId());

        Assertions.assertEquals(bookingList.get(1).getBookingStatus(), bookingDtoList.get(1).getBookingStatus());
    }

    @Test
    void shouldConvertBookingDTOListToBookingList() throws Exception {
        List<BookingDto> bookingDtoList = new ArrayList<>();

        Passenger passenger1 = new Passenger();
        passenger1.setId(1001L);
        when(passengerServiceMock.getPassenger(1001L)).thenReturn(Optional.of(passenger1));
        Passenger passenger2 = new Passenger();
        passenger2.setId(1002L);
        when(passengerServiceMock.getPassenger(1002L)).thenReturn(Optional.of(passenger2));

        FlightSeat flightSeat1 = new FlightSeat();
        Long flightSeatId1 = 2L;
        flightSeat1.setId(flightSeatId1);
        when(flightSeatServiceMock.getFlightSeat(flightSeatId1)).thenReturn(Optional.of(flightSeat1));
        FlightSeat flightSeat2 = new FlightSeat();
        Long flightSeatId2 = 4L;
        flightSeat2.setId(flightSeatId2);
        when(flightSeatServiceMock.getFlightSeat(flightSeatId2)).thenReturn(Optional.of(flightSeat2));

        LocalDateTime createTime = LocalDateTime.MIN;

        BookingDto bookingDtoOne = new BookingDto();
        bookingDtoOne.setId(1L);
        bookingDtoOne.setBookingDate(LocalDateTime.now());
        bookingDtoOne.setFlightSeatId(flightSeatId1);
        bookingDtoOne.setBookingStatus(BookingStatus.NOT_PAID);
        bookingDtoOne.setPassengerId(passenger1.getId());

        BookingDto bookingDtoTwo = new BookingDto();
        bookingDtoTwo.setId(2L);
        bookingDtoTwo.setBookingDate(LocalDateTime.now());
        bookingDtoTwo.setFlightSeatId(flightSeatId2);
        bookingDtoTwo.setBookingStatus(BookingStatus.PAID);
        bookingDtoTwo.setPassengerId(passenger2.getId());

        bookingDtoList.add(bookingDtoOne);
        bookingDtoList.add(bookingDtoTwo);

        List<Booking> bookingList = bookingMapper.toEntityList(bookingDtoList);

        Assertions.assertEquals(bookingList.size(), bookingDtoList.size());
        Assertions.assertEquals(bookingDtoList.get(0).getId(), bookingList.get(0).getId());
        Assertions.assertEquals(bookingDtoList.get(0).getBookingDate(), bookingList.get(0).getBookingDate());
        Assertions.assertEquals(bookingDtoList.get(0).getFlightSeatId(), bookingList.get(0).getFlightSeat().getId());
        Assertions.assertEquals(bookingDtoList.get(0).getBookingStatus(), bookingList.get(0).getBookingStatus());
        Assertions.assertEquals(bookingDtoList.get(1).getId(), bookingList.get(1).getId());
        Assertions.assertEquals(bookingDtoList.get(1).getBookingDate(), bookingList.get(1).getBookingDate());
        Assertions.assertEquals(bookingDtoList.get(1).getFlightSeatId(), bookingList.get(1).getFlightSeat().getId());
        Assertions.assertEquals(bookingDtoList.get(1).getBookingStatus(), bookingList.get(1).getBookingStatus());
    }
}