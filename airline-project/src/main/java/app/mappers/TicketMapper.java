package app.mappers;

import app.dto.TicketDto;
import app.entities.Ticket;
import app.services.interfaces.FlightSeatService;
import app.services.interfaces.FlightService;
import app.services.interfaces.PassengerService;
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
    @Mapping(target = "flightId", expression = "java(ticket.getFlight().getId())")
    @Mapping(target = "code", expression = "java(ticket.getFlight().getCode())")
    @Mapping(target = "from", expression = "java(ticket.getFlight().getFrom().getAirportCode())")
    @Mapping(target = "to", expression = "java(ticket.getFlight().getTo().getAirportCode())")
    @Mapping(target = "departureDateTime", expression = "java(ticket.getFlight().getDepartureDateTime())")
    @Mapping(target = "arrivalDateTime", expression = "java(ticket.getFlight().getArrivalDateTime())")
    @Mapping(target = "flightSeatId", expression = "java(ticket.getFlightSeat().getId())")
    @Mapping(target = "seatNumber", expression = "java(ticket.getFlightSeat().getSeat().getSeatNumber())")
    TicketDto convertToTicketDto(Ticket ticket);

    @Mapping(target = "passenger", expression = "java(passengerService.getPassengerById(ticketDto.getPassengerId()).get())")
    @Mapping(target = "flight", expression = "java(flightService.getFlightById(ticketDto.getFlightId()).get())")
    @Mapping(target = "flightSeat", expression = "java(flightSeatService.getFlightSeatById(ticketDto.getFlightSeatId()).get())")
    Ticket convertToTicketEntity(TicketDto ticketDto,
                                 @Context PassengerService passengerService,
                                 @Context FlightService flightService,
                                 @Context FlightSeatService flightSeatService);

    default List<TicketDto> convertToTicketDtoList(List<Ticket> ticketList) {
        return ticketList.stream()
                .map(this::convertToTicketDto)
                .collect(Collectors.toList());
    }

    default List<Ticket> convertToTicketEntityList(List<TicketDto> ticketDtoList,
                                                   PassengerService passengerService,
                                                   FlightService flightService,
                                                   FlightSeatService flightSeatService) {
        return ticketDtoList.stream()
                .map(ticketDto -> convertToTicketEntity(ticketDto, passengerService, flightService, flightSeatService))
                .collect(Collectors.toList());
    }
}