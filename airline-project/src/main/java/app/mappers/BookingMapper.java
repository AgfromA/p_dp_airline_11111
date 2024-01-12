package app.mappers;

import app.dto.BookingDto;
import app.entities.Booking;

import app.services.interfaces.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Context;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    @Mapping(target = "passengerId", expression = "java(booking.getPassenger().getId())")
    @Mapping(target = "flightSeatId", expression = "java(booking.getFlightSeat().getId())")
    BookingDto convertToBookingDtoEntity(Booking booking);

    @Mapping(target = "passenger", expression = "java(passengerService.getPassengerById(bookingDto.getPassengerId()).get())")
    @Mapping(target = "flightSeat", expression = "java(flightSeatService.getFlightSeatById(bookingDto.getFlightSeatId()).get())")
    Booking convertToBookingEntity(BookingDto bookingDto,
                                   @Context PassengerService passengerService,
                                   @Context FlightSeatService flightSeatService);

    default List<BookingDto> convertToBookingDtoEntityList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::convertToBookingDtoEntity)
                .collect(Collectors.toList());
    }

    default List<Booking> convertToBookingEntityList(List<BookingDto> bookingDtos,
                                                     PassengerService passengerService,
                                                     FlightSeatService flightSeatService) {
        return bookingDtos.stream()
                .map(bookingDTO -> convertToBookingEntity(bookingDTO, passengerService, flightSeatService))
                .collect(Collectors.toList());
    }
}