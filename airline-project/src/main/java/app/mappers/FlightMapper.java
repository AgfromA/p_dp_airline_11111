package app.mappers;

import app.dto.FlightDto;
import app.dto.FlightSeatDto;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.services.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;


import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class FlightMapper {

    @Autowired
    protected AircraftService aircraftService;
    @Autowired
    protected DestinationService destinationService;
    @Lazy
    @Autowired
    protected FlightSeatService flightSeatService;
    @Autowired
    protected FlightService flightService;
    @Autowired
    protected SeatService seatService;

    @Mapping(target = "aircraft", expression = "java(aircraftService.getAircraft(flightDto.getAircraftId()))")
    @Mapping(target = "from", expression = "java(destinationService.getDestinationByAirportCode(flightDto.getAirportFrom()))")
    @Mapping(target = "to", expression = "java(destinationService.getDestinationByAirportCode(flightDto.getAirportTo()))")
    @Mapping(target = "seats", expression = "java(flightSeatService.findByFlightId(flightDto.getId()))")
    public abstract Flight toEntity(FlightDto flightDto);

    @Mapping(target = "aircraftId", source = "aircraft.id")
    @Mapping(target = "airportFrom", source = "from.airportCode")
    @Mapping(target = "airportTo", source = "to.airportCode")
    @Mapping(target = "seats", expression = "java(toFlightSeatsDtoList(flight.getSeats()))")
    public abstract FlightDto toDto(Flight flight);

    public List<FlightSeatDto> toFlightSeatsDtoList(List<FlightSeat> flightSeats) {
        if (flightSeats == null) {
            return null;
        }
        return flightSeats.stream()
                .map(flightSeat -> Mappers.getMapper(FlightSeatMapper.class).toDto(flightSeat))
                .collect(Collectors.toList());
    }

    public List<FlightDto> toDtoList(List<Flight> flights) {
        return flights.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Flight> toEntityList(List<FlightDto> flightDtoList) {
        return flightDtoList.stream()
                .map(flightDto -> toEntity(flightDto))
                .collect(Collectors.toList());

    }
}
