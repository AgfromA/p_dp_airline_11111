package app.mappers;

import app.dto.BookingDTO;
import app.entities.Booking;

import app.services.interfaces.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Context;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    @Mapping(target = "passengerId", expression = "java(booking.getPassenger().getId())")
    @Mapping(target = "flightSeatId", expression = "java(booking.getFlightSeat().getId())")
    BookingDTO convertToBookingDTOEntity(Booking booking);

    @Mapping(target = "passenger", expression = "java(passengerService.getPassengerById(bookingDTO.getPassengerId()).get())")
    @Mapping(target = "flightSeat", expression = "java(flightSeatService.getFlightSeatById(bookingDTO.getFlightSeatId()).get())")
    Booking convertToBookingEntity(BookingDTO bookingDTO, @Context PassengerService passengerService,
                                   @Context FlightSeatService flightSeatService);

}
