package app.mappers;

import app.dto.FlightDTO;
import app.dto.FlightSeatDTO;
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

    @Mapping(target = "seats", expression = "java(flightSeatService.findByFlightId(flightDTO.getId()))")
    @Mapping(target = "ticket", expression = "java(ticketService.findByFlightId(flightDTO.getId()))")
    @Mapping(target = "from", expression = "java(destinationService.getDestinationByAirportCode(flightDTO.getAirportFrom()))")
    @Mapping(target = "to", expression = "java(destinationService.getDestinationByAirportCode(flightDTO.getAirportTo()))")
    @Mapping(target = "aircraft", expression = "java(aircraftService.getAircraftById(flightDTO.getAircraftId()))")
    Flight flightDTOtoFlight(FlightDTO flightDTO, @Context AircraftService aircraftService,
                             @Context DestinationService destinationService, @Context TicketService ticketService,
                             @Context FlightSeatService flightSeatService);

    @Mapping(target = "airportFrom", expression = "java(flight.getFrom().getAirportCode())")
    @Mapping(target = "airportTo", expression = "java(flight.getTo().getAirportCode())")
    @Mapping(target = "aircraftId", expression = "java(flight.getAircraft().getId())")
    @Mapping(target = "seats", expression = "java(toFlightSeatsDTOList(flight.getSeats(), flightService))")
    FlightDTO flightToFlightDTO(Flight flight, @Context FlightService flightService);

    default List<FlightSeatDTO> toFlightSeatsDTOList(List<FlightSeat> flightSeats, FlightService flightService) {
        return flightSeats.stream().map(flightSeat -> Mappers.getMapper(FlightSeatMapper.class)
                .convertToFlightSeatDTOEntity(flightSeat, flightService)).collect(Collectors.toList());
    }

    default List<Flight> convertFlightDTOListToFlightList(List<FlightDTO> flightDTOList, AircraftService aircraftService,
                                                          DestinationService destinationService, TicketService ticketService,
                                                          FlightSeatService flightSeatService) {

        return flightDTOList.stream()
                .map(flightDTO -> flightDTOtoFlight(flightDTO, aircraftService, destinationService, ticketService, flightSeatService))
                .collect(Collectors.toList());
    }

    default List<FlightDTO> convertFlightListToFlighDTOtList(List<Flight> flights, FlightService flightService) {
        return flights.stream()
                .map(flight -> flightToFlightDTO(flight, flightService))
                .collect(Collectors.toList());
    }
}
