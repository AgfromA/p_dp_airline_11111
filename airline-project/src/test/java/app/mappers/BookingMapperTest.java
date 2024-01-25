package app.mappers;

import app.dto.BookingDto;
import app.entities.*;
import app.enums.BookingStatus;
import app.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


class BookingMapperTest {

    BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    @Mock
    private PassengerService passengerServiceMock = Mockito.mock(PassengerService.class);

    @Mock
    private FlightSeatService flightSeatServiceMock = Mockito.mock(FlightSeatService.class);

    @Test
    void shouldConvertBookingToBookingDTOEntity() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        when(passengerServiceMock.getPassenger(1001L)).thenReturn(Optional.of(passenger));

        FlightSeat flightSeat = new FlightSeat();
        flightSeat.setId(2L);

        LocalDateTime createTime = LocalDateTime.MIN;

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookingNumber("BK-111111");
        booking.setBookingDate(LocalDateTime.now());
        booking.setPassenger(passengerServiceMock.getPassenger(1001L).get());
        booking.setFlightSeat(flightSeat);
        booking.setCreateTime(createTime);
        booking.setBookingStatus(BookingStatus.NOT_PAID);

        BookingDto bookingDTO = bookingMapper.toDto(booking);

        Assertions.assertNotNull(bookingDTO);
        Assertions.assertEquals(booking.getId(), bookingDTO.getId());
        Assertions.assertEquals(booking.getBookingNumber(), bookingDTO.getBookingNumber());
        Assertions.assertEquals(booking.getBookingDate(), bookingDTO.getBookingDate());
        Assertions.assertEquals(booking.getPassenger().getId(), bookingDTO.getPassengerId());
        Assertions.assertEquals(booking.getFlightSeat().getId(), bookingDTO.getFlightSeatId());
        Assertions.assertEquals(booking.getCreateTime(), bookingDTO.getCreateTime());
        Assertions.assertEquals(booking.getBookingStatus(), bookingDTO.getBookingStatus());

    }

    @Test
    void shouldConvertBookingDTOToBookingEntity() throws Exception {

        Passenger passenger = new Passenger();
        passenger.setId(1001L);
        when(passengerServiceMock.getPassenger(1001L)).thenReturn(Optional.of(passenger));

        LocalDateTime createTime = LocalDateTime.MIN;

        FlightSeat flightSeat = new FlightSeat();
        Long flightSeatId = 2L;
        flightSeat.setId(flightSeatId);

        BookingDto bookingDTO = new BookingDto();
        bookingDTO.setId(1L);
        bookingDTO.setBookingNumber("BK-111111");
        bookingDTO.setBookingDate(LocalDateTime.now());
        bookingDTO.setPassengerId(passengerServiceMock.getPassenger(1001L).get().getId());
        bookingDTO.setCreateTime(createTime);
        bookingDTO.setFlightSeatId(flightSeatId);
        bookingDTO.setBookingStatus(BookingStatus.NOT_PAID);

        when(flightSeatServiceMock.getFlightSeat(flightSeatId)).thenReturn(Optional.of(flightSeat));

        Booking booking = bookingMapper.toEntity(bookingDTO, passengerServiceMock, flightSeatServiceMock);

        Assertions.assertNotNull(booking);
        Assertions.assertEquals(bookingDTO.getId(), booking.getId());
        Assertions.assertEquals(bookingDTO.getBookingNumber(), booking.getBookingNumber());
        Assertions.assertEquals(bookingDTO.getBookingDate(), booking.getBookingDate());
        Assertions.assertEquals(bookingDTO.getPassengerId(), booking.getPassenger().getId());
        Assertions.assertEquals(bookingDTO.getCreateTime(), booking.getCreateTime());
        Assertions.assertEquals(bookingDTO.getBookingStatus(), booking.getBookingStatus());
        Assertions.assertEquals(bookingDTO.getFlightSeatId(), booking.getFlightSeat().getId());
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
        bookingOne.setBookingNumber("BK-111111");
        bookingOne.setBookingDate(LocalDateTime.now());
        bookingOne.setPassenger(passengerServiceMock.getPassenger(1001L).get());
        bookingOne.setFlightSeat(flightSeat1);
        bookingOne.setCreateTime(createTime);
        bookingOne.setBookingStatus(BookingStatus.NOT_PAID);

        Booking bookingTwo = new Booking();
        bookingTwo.setId(2L);
        bookingTwo.setBookingNumber("BK-211112");
        bookingTwo.setBookingDate(LocalDateTime.now());
        bookingTwo.setPassenger(passengerServiceMock.getPassenger(1002L).get());
        bookingTwo.setFlightSeat(flightSeat2);
        bookingTwo.setCreateTime(createTime);
        bookingTwo.setBookingStatus(BookingStatus.PAID);

        bookingList.add(bookingOne);
        bookingList.add(bookingTwo);

        List<BookingDto> bookingDtoList = bookingMapper.toDtoList(bookingList);

        Assertions.assertEquals(bookingList.size(), bookingDtoList.size());

        Assertions.assertEquals(bookingList.get(0).getId(), bookingDtoList.get(0).getId());
        Assertions.assertEquals(bookingList.get(0).getBookingNumber(), bookingDtoList.get(0).getBookingNumber());
        Assertions.assertEquals(bookingList.get(0).getBookingDate(), bookingDtoList.get(0).getBookingDate());
        Assertions.assertEquals(bookingList.get(0).getPassenger().getId(), bookingDtoList.get(0).getPassengerId());
        Assertions.assertEquals(bookingList.get(0).getFlightSeat().getId(), bookingDtoList.get(0).getFlightSeatId());
        Assertions.assertEquals(bookingList.get(0).getCreateTime(), bookingDtoList.get(0).getCreateTime());
        Assertions.assertEquals(bookingList.get(0).getBookingStatus(), bookingDtoList.get(0).getBookingStatus());

        Assertions.assertEquals(bookingList.get(1).getId(), bookingDtoList.get(1).getId());
        Assertions.assertEquals(bookingList.get(1).getBookingNumber(), bookingDtoList.get(1).getBookingNumber());
        Assertions.assertEquals(bookingList.get(1).getBookingDate(), bookingDtoList.get(1).getBookingDate());
        Assertions.assertEquals(bookingList.get(1).getPassenger().getId(), bookingDtoList.get(1).getPassengerId());
        Assertions.assertEquals(bookingList.get(1).getFlightSeat().getId(), bookingDtoList.get(1).getFlightSeatId());
        Assertions.assertEquals(bookingList.get(1).getCreateTime(), bookingDtoList.get(1).getCreateTime());
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
        bookingDtoOne.setBookingNumber("BK-111111");
        bookingDtoOne.setBookingDate(LocalDateTime.now());
        bookingDtoOne.setPassengerId(passengerServiceMock.getPassenger(1001L).get().getId());
        bookingDtoOne.setCreateTime(createTime);
        bookingDtoOne.setFlightSeatId(flightSeatId1);
        bookingDtoOne.setBookingStatus(BookingStatus.NOT_PAID);

        BookingDto bookingDtoTwo = new BookingDto();
        bookingDtoTwo.setId(2L);
        bookingDtoTwo.setBookingNumber("BK-211112");
        bookingDtoTwo.setBookingDate(LocalDateTime.now());
        bookingDtoTwo.setPassengerId(passengerServiceMock.getPassenger(1002L).get().getId());
        bookingDtoTwo.setCreateTime(createTime);
        bookingDtoTwo.setFlightSeatId(flightSeatId2);
        bookingDtoTwo.setBookingStatus(BookingStatus.PAID);

        bookingDtoList.add(bookingDtoOne);
        bookingDtoList.add(bookingDtoTwo);

        List<Booking> bookingList = bookingMapper.toEntityList(bookingDtoList, passengerServiceMock,
                flightSeatServiceMock);

        Assertions.assertEquals(bookingList.size(), bookingDtoList.size());

        Assertions.assertEquals(bookingDtoList.get(0).getId(), bookingList.get(0).getId());
        Assertions.assertEquals(bookingDtoList.get(0).getBookingNumber(), bookingList.get(0).getBookingNumber());
        Assertions.assertEquals(bookingDtoList.get(0).getBookingDate(), bookingList.get(0).getBookingDate());
        Assertions.assertEquals(bookingDtoList.get(0).getPassengerId(), bookingList.get(0).getPassenger().getId());
        Assertions.assertEquals(bookingDtoList.get(0).getFlightSeatId(), bookingList.get(0).getFlightSeat().getId());
        Assertions.assertEquals(bookingDtoList.get(0).getCreateTime(), bookingList.get(0).getCreateTime());
        Assertions.assertEquals(bookingDtoList.get(0).getBookingStatus(), bookingList.get(0).getBookingStatus());

        Assertions.assertEquals(bookingDtoList.get(1).getId(), bookingList.get(1).getId());
        Assertions.assertEquals(bookingDtoList.get(1).getBookingNumber(), bookingList.get(1).getBookingNumber());
        Assertions.assertEquals(bookingDtoList.get(1).getBookingDate(), bookingList.get(1).getBookingDate());
        Assertions.assertEquals(bookingDtoList.get(1).getPassengerId(), bookingList.get(1).getPassenger().getId());
        Assertions.assertEquals(bookingDtoList.get(1).getFlightSeatId(), bookingList.get(1).getFlightSeat().getId());
        Assertions.assertEquals(bookingDtoList.get(1).getCreateTime(), bookingList.get(1).getCreateTime());
        Assertions.assertEquals(bookingDtoList.get(1).getBookingStatus(), bookingList.get(1).getBookingStatus());
    }
}
