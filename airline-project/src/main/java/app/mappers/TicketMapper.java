package app.mappers;

import app.dto.TicketDto;
import app.entities.Ticket;
import app.services.BookingService;
import app.services.FlightSeatService;
import app.services.FlightService;
import app.services.PassengerService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapper {

    @Mapping(target = "passengerId", expression = "java(ticket.getPassenger().getId())")
    @Mapping(target = "firstName", expression = "java(ticket.getPassenger().getFirstName())")
    @Mapping(target = "lastName", expression = "java(ticket.getPassenger().getLastName())")
    @Mapping(target = "code", expression = "java(ticket.getFlightSeat().getFlight().getCode())")
    @Mapping(target = "from", expression = "java(ticket.getFlightSeat().getFlight().getFrom().getAirportCode())")
    @Mapping(target = "to", expression = "java(ticket.getFlightSeat().getFlight().getTo().getAirportCode())")
    @Mapping(target = "departureDateTime", expression = "java(ticket.getFlightSeat().getFlight().getDepartureDateTime())")
    @Mapping(target = "arrivalDateTime", expression = "java(ticket.getFlightSeat().getFlight().getArrivalDateTime())")
    @Mapping(target = "flightSeatId", expression = "java(ticket.getFlightSeat().getId())")
    @Mapping(target = "seatNumber", expression = "java(ticket.getFlightSeat().getSeat().getSeatNumber())")
    @Mapping(target = "bookingId",expression ="java(ticket.getBooking().getId())")
    @Mapping(target = "boardingStartTime",expression ="java(ticket.getFlightSeat().getFlight().getDepartureDateTime()." +
            "minusMinutes(40))")
    @Mapping(target = "boardingEndTime",expression ="java(ticket.getFlightSeat().getFlight().getDepartureDateTime()." +
            "minusMinutes(20))")
    TicketDto toDto(Ticket ticket);

    @Mapping(target = "passenger", expression = "java(passengerService.getPassenger(ticketDto.getPassengerId()).get())")
    @Mapping(target = "flightSeat", expression = "java(flightSeatService.getFlightSeat(ticketDto.getFlightSeatId()).get())")
    @Mapping(target = "booking", expression = "java(bookingService.getBooking(ticketDto.getBookingId()).get())")
    Ticket toEntity(TicketDto ticketDto,
                    @Context PassengerService passengerService,
                    @Context FlightService flightService,
                    @Context FlightSeatService flightSeatService,
                    @Context BookingService bookingService);

    default List<TicketDto> toDtoList(List<Ticket> ticketList) {
        return ticketList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    default List<Ticket> toEntityList(List<TicketDto> ticketDtoList,
                                      PassengerService passengerService,
                                      FlightService flightService,
                                      FlightSeatService flightSeatService,
                                      BookingService bookingService) {
        return ticketDtoList.stream()
                .map(ticketDto -> toEntity(ticketDto, passengerService, flightService, flightSeatService, bookingService))
                .collect(Collectors.toList());
    }
}