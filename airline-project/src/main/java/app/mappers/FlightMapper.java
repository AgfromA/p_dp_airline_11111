package app.mappers;

import app.dto.FlightDto;
import app.dto.FlightSeatDto;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.services.interfaces.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FlightMapper {

    @Mapping(target = "seats", expression = "java(flightSeatService.findByFlightId(flightDto.getId()))")
    @Mapping(target = "ticket", expression = "java(ticketService.findByFlightId(flightDto.getId()))")
    @Mapping(target = "from", expression = "java(destinationService.getDestinationByAirportCode(flightDto.getAirportFrom()))")
    @Mapping(target = "to", expression = "java(destinationService.getDestinationByAirportCode(flightDto.getAirportTo()))")
    @Mapping(target = "aircraft", expression = "java(aircraftService.getAircraftById(flightDto.getAircraftId()))")
    Flight flightDtotoFlight(FlightDto flightDto,
                             @Context AircraftService aircraftService,
                             @Context DestinationService destinationService,
                             @Context TicketService ticketService,
                             @Context FlightSeatService flightSeatService);

    @Mapping(target = "airportFrom", expression = "java(flight.getFrom().getAirportCode())")
    @Mapping(target = "airportTo", expression = "java(flight.getTo().getAirportCode())")
    @Mapping(target = "aircraftId", expression = "java(flight.getAircraft().getId())")
    @Mapping(target = "seats", expression = "java(toFlightSeatsDtoList(flight.getSeats(), flightService))")
    FlightDto flightToFlightDto(Flight flight, @Context FlightService flightService);

    default List<FlightSeatDto> toFlightSeatsDtoList(List<FlightSeat> flightSeats, FlightService flightService) {
        return flightSeats.stream().map(flightSeat -> Mappers.getMapper(FlightSeatMapper.class)
                .convertToFlightSeatDtoEntity(flightSeat, flightService)).collect(Collectors.toList());
    }

    default List<Flight> convertFlightDtoListToFlightList(List<FlightDto> flightDtoList, AircraftService aircraftService,
                                                          DestinationService destinationService, TicketService ticketService,
                                                          FlightSeatService flightSeatService) {

        return flightDtoList.stream()
                .map(flightDto -> flightDtotoFlight(flightDto, aircraftService, destinationService, ticketService, flightSeatService))
                .collect(Collectors.toList());
    }

    default List<FlightDto> convertFlightListToFlighDtotList(List<Flight> flights, FlightService flightService) {
        return flights.stream()
                .map(flight -> flightToFlightDto(flight, flightService))
                .collect(Collectors.toList());
    }
}