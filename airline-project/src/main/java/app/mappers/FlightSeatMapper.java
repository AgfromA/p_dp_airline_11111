package app.mappers;

import app.dto.FlightSeatDTO;
import app.dto.SeatDTO;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.services.interfaces.FlightService;
import app.services.interfaces.SeatService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlightSeatMapper {

    FlightSeatMapper INSTANCE = Mappers.getMapper(FlightSeatMapper.class);

    @Mapping(target = "flightId", expression = "java(flightService.getFlightById(flightSeat.getFlight().getId()).get().getId())")
    @Mapping(target = "category", expression = "java(flightSeat.getSeat().getCategory().getCategoryType())")
    @Mapping(target = "seat", expression = "java(toSeatDTO(flightSeat.getSeat()))")
    FlightSeatDTO convertToFlightSeatDTOEntity(FlightSeat flightSeat, FlightService flightService);

    @Mapping(target = "flight", expression = "java(flightService.getFlightById(dto.getFlightId()).get())")
    @Mapping(target = "seat", expression = "java(seatService.getSeatById(dto.getSeat().getId()))")
    FlightSeat convertToFlightSeatEntity(FlightSeatDTO dto, FlightService flightService, SeatService seatService);

    default SeatDTO toSeatDTO(Seat seat) {
        return SeatMapper.INSTANCE.convertToSeatDTOEntity(seat);
    }
}
