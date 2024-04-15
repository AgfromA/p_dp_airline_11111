package app.mappers;

import app.dto.BookingDto;
import app.entities.Booking;

import app.services.FlightSeatService;
import app.services.FlightService;
import app.services.PassengerService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class BookingMapper {

    @Autowired
    protected PassengerService passengerService;
    @Autowired
    protected FlightSeatService flightSeatService;
    @Autowired
    protected FlightService flightService;

    @Mapping(target = "passengerId", expression = "java(booking.getPassenger().getId())")
    @Mapping(target = "flightSeatId", expression = "java(booking.getFlightSeat().getId())")
    @Mapping(target = "flightId", expression = "java(booking.getFlightSeat() != null && booking.getFlightSeat().getFlight() != null ? booking.getFlightSeat().getFlight().getId() : null)")
    public abstract BookingDto toDto(Booking booking);

    @Mapping(target = "passenger", expression = "java(passengerService.getPassenger(bookingDto.getPassengerId()).get())")
    @Mapping(target = "flightSeat", expression = "java(flightSeatService.getFlightSeat(bookingDto.getFlightSeatId()).get())")
    @Mapping(target = "flightSeat.flight", expression = "java(flightService.getFlight(bookingDto.getFlightId()).get())")
    public abstract Booking toEntity(BookingDto bookingDto);

    public List<BookingDto> toDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Booking> toEntityList(List<BookingDto> bookingDtos) {
        return bookingDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}