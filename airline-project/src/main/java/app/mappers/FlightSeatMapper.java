package app.mappers;

import app.dto.FlightSeatDTO;
import app.entities.FlightSeat;
import app.services.interfaces.FlightService;
import app.services.interfaces.SeatService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlightSeatMapper {

    FlightSeatMapper INSTANCE = Mappers.getMapper(FlightSeatMapper.class);

    @Mapping(target = "flightId", expression = "java(flightService.getFlightById(flightSeat.getFlight().getId()).get().getId())")
    @Mapping(target = "seatNumber", expression = "java(flightSeat.getSeat().getSeatNumber())")
    @Mapping(target = "category", expression = "java(seatService.getSeatById(flightSeat.getSeat().getId()).getCategory().getCategoryType())")
    FlightSeatDTO convertToFlightSeatDTOEntity(FlightSeat flightSeat, FlightService flightService, SeatService seatService);

    @Mapping(target = "flight", expression = "java(flightService.getFlightById(dto.getFlightId()).get())")
    @Mapping(target = "seat", expression = "java(seatService.getSeatBySeatNumber(dto.getSeatNumber()))")
    FlightSeat convertToFlightSeatEntity(FlightSeatDTO dto,FlightService flightService,SeatService seatService);

}
