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

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FlightSeatMapper {

    SeatMapper seatMapper = Mappers.getMapper(SeatMapper.class);

    @Mapping(target = "flightId", expression = "java(flightService.getFlightById(flightSeat.getFlight().getId()).get().getId())")
    @Mapping(target = "category", expression = "java(flightSeat.getSeat().getCategory().getCategoryType())")
    @Mapping(target = "seat", expression = "java(toSeatDto(flightSeat.getSeat()))")
    FlightSeatDto toDto(FlightSeat flightSeat, @Context FlightService flightService);

    @Mapping(target = "flight", expression = "java(flightService.getFlightById(flightSeatDto.getFlightId()).get())")
    @Mapping(target = "seat", expression = "java(seatService.getSeat(flightSeatDto.getSeat().getId()))")
    FlightSeat toEntity(FlightSeatDto flightSeatDto,
                        @Context FlightService flightService,
                        @Context SeatService seatService);

    default SeatDto toSeatDto(Seat seat) {
        return seatMapper.toDto(seat);
    }

    default List<FlightSeatDto> toDtoList(List<FlightSeat> flightSeats, FlightService flightService) {
        return flightSeats.stream()
                .map(flightSeat -> toDto(flightSeat, flightService))
                .collect(Collectors.toList());
    }

    default List<FlightSeat> toEntityList(List<FlightSeatDto> flightSeatDtoList, FlightService flightService,
                                          SeatService seatService) {
        return flightSeatDtoList.stream()
                .map(flightSeatDTO -> toEntity(flightSeatDTO, flightService, seatService))
                .collect(Collectors.toList());
    }
}