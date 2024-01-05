package app.mappers;

import app.dto.BookingDTO;
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
    BookingDTO convertToBookingDTOEntity(Booking booking);

    @Mapping(target = "passenger", expression = "java(passengerService.getPassengerById(bookingDTO.getPassengerId()).get())")
    @Mapping(target = "flightSeat", expression = "java(flightSeatService.getFlightSeatById(bookingDTO.getFlightSeatId()).get())")
    Booking convertToBookingEntity(BookingDTO bookingDTO, @Context PassengerService passengerService,
                                   @Context FlightSeatService flightSeatService);

    default List<BookingDTO> convertToBookingDTOEntityList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::convertToBookingDTOEntity)
                .collect(Collectors.toList());
    }

    default List<Booking> convertToBookingEntityList(List<BookingDTO> bookingDTOs, PassengerService passengerService,
                                                     FlightSeatService flightSeatService) {
        return bookingDTOs.stream()
                .map(bookingDTO -> convertToBookingEntity(bookingDTO, passengerService, flightSeatService))
                .collect(Collectors.toList());
    }
}
