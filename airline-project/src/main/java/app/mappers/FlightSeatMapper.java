package app.mappers;

import app.dto.FlightSeatDto;
import app.dto.SeatDto;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.services.FlightService;
import app.services.SeatService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class FlightSeatMapper {
    @Lazy
    @Autowired
    protected FlightService flightService;
    @Autowired
    protected SeatService seatService;

    SeatMapper seatMapper = Mappers.getMapper(SeatMapper.class);

    @Mapping(target = "flightId", expression = "java(flightSeat.getFlight().getId())")
    @Mapping(target = "category", expression = "java(flightSeat.getSeat().getCategory().getCategoryType())")
    @Mapping(target = "seat", expression = "java(toSeatDto(flightSeat.getSeat()))")
    public abstract FlightSeatDto toDto(FlightSeat flightSeat);

    @Mapping(target = "flight", expression = "java(flightService.getFlight(flightSeatDto.getFlightId()).get())")
    @Mapping(target = "seat", expression = "java(seatService.getSeat(flightSeatDto.getSeat().getId()))")
    public abstract FlightSeat toEntity(FlightSeatDto flightSeatDto);

    public SeatDto toSeatDto(Seat seat) {
        return seatMapper.toDto(seat);
    }

    public List<FlightSeatDto> toDtoList(List<FlightSeat> flightSeats) {
        return flightSeats.stream()
                .map(flightSeat -> toDto(flightSeat))
                .collect(Collectors.toList());
    }

    public List<FlightSeat> toEntityList(List<FlightSeatDto> flightSeatDtoList) {
        return flightSeatDtoList.stream()
                .map(flightSeatDTO -> toEntity(flightSeatDTO))
                .collect(Collectors.toList());
    }
}