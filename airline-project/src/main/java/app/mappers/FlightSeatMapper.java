package app.mappers;

import app.dto.FlightSeatDTO;
import app.dto.SeatDTO;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.services.interfaces.FlightService;
import app.services.interfaces.SeatService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface FlightSeatMapper {

    FlightSeatMapper INSTANCE = Mappers.getMapper(FlightSeatMapper.class);

    @Mapping(target = "flightId", expression = "java(flightService.getFlightById(flightSeat.getFlight().getId()).get().getId())")
    @Mapping(target = "category", expression = "java(flightSeat.getSeat().getCategory().getCategoryType())")
    @Mapping(target = "seat", expression = "java(toSeatDTO(flightSeat.getSeat()))")
    FlightSeatDTO convertToFlightSeatDTOEntity(FlightSeat flightSeat, @Context FlightService flightService);

    @Mapping(target = "flight", expression = "java(flightService.getFlightById(dto.getFlightId()).get())")
    @Mapping(target = "seat", expression = "java(seatService.getSeatById(dto.getSeat().getId()))")
    FlightSeat convertToFlightSeatEntity(FlightSeatDTO dto, @Context FlightService flightService, @Context SeatService seatService);

    default SeatDTO toSeatDTO(Seat seat) {
        return SeatMapper.INSTANCE.convertToSeatDTOEntity(seat);
    }

    default List<FlightSeatDTO> convertToFlightSeatDTOList(List<FlightSeat> flightSeats, FlightService flightService) {
        return flightSeats.stream()
                .map(flightSeat -> convertToFlightSeatDTOEntity(flightSeat, flightService))
                .collect(Collectors.toList());
    }

    default List<FlightSeat> convertToFlightSeatEntityList(List<FlightSeatDTO> flightSeatDTOList, FlightService flightService,
                                                           SeatService seatService) {
        return flightSeatDTOList.stream()
                .map(flightSeatDTO -> convertToFlightSeatEntity(flightSeatDTO, flightService, seatService))
                .collect(Collectors.toList());
    }
}
