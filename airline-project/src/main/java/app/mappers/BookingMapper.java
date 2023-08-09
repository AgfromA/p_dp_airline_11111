package app.mappers;

import app.dto.BookingDTO;
import app.entities.Booking;

import app.services.interfaces.CategoryService;
import app.services.interfaces.FlightService;
import app.services.interfaces.PassengerService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Context;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "passengerId", expression = "java((passengerService.getPassengerById(booking.getPassenger().getId())" +
                                                  ".get()).getId())")
    @Mapping(target = "flightId", expression = "java(flightService.getFlightById(booking.getFlight().getId()).get().getId())")
    @Mapping(target = "categoryType", expression = "java(categoryService.getCategoryByType(booking.getCategory()" +
                                                   ".getCategoryType()).getCategoryType())")
    BookingDTO convertToBookingDTOEntity(Booking booking, @Context PassengerService passengerService,
                                         @Context FlightService flightService, @Context CategoryService categoryService);

    @Mapping(target = "passenger", expression = "java(passengerService.getPassengerById(bookingDTO.getPassengerId()).get())")
    @Mapping(target = "flight", expression = "java(flightService.getFlightById(bookingDTO.getFlightId()).get())")
    @Mapping(target = "category", expression = "java(categoryService.getCategoryByType(bookingDTO.getCategoryType()))")
    Booking convertToBookingEntity(BookingDTO bookingDTO, @Context PassengerService passengerService,
                                   @Context FlightService flightService, @Context CategoryService categoryService);

}
