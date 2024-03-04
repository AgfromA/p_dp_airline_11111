package app.mappers;

import app.dto.BookingDto;
import app.entities.Booking;

import app.services.FlightSeatService;
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

    @Mapping(target = "passengerId", expression = "java(booking.getPassenger().getId())")
    @Mapping(target = "flightSeatId", expression = "java(booking.getFlightSeat().getId())")
    public abstract BookingDto toDto(Booking booking);

    @Mapping(target = "passenger", expression = "java(passengerService.getPassenger(bookingDto.getPassengerId()).get())")
    @Mapping(target = "flightSeat", expression = "java(flightSeatService.getFlightSeat(bookingDto.getFlightSeatId()).get())")
    public abstract Booking toEntity(BookingDto bookingDto);

    public List<BookingDto> toDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Booking> toEntityList(List<BookingDto> bookingDtos) {
        return bookingDtos.stream()
                .map(bookingDTO -> toEntity(bookingDTO))
                .collect(Collectors.toList());
    }

}