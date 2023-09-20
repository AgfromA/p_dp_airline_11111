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
    /* TODO кажется стоит сделать так:: @Mapping(target = "flightId", expression = "java(flightSeat.getFlight().getId())")*/
    @Mapping(target = "seatNumber", expression = "java(seatService.getSeatById(flightSeat.getSeat().getId()).getId())")
    /*  todo кажется корректнее так
            @Mapping(target = "seatNumber", expression = "java(seatService.getSeatById(flightSeat.getSeat().getId()).getSeatNumber())")
            или
            @Mapping(target = "seatNumber", expression = "java(flightSeat.getSeat().getId())")
            вероятнее всего вариант с getSeatNumber(), потому что
            см. https://www.google.com/search?client=opera-gx&q=нумерация+мест+в+самолете&sourceid=opera&ie=UTF-8&oe=UTF-8#vhid=kjYjv5EpclxT_M&vssid=l
    */
    FlightSeatDTO convertToFlightSeatDTOEntity(FlightSeat flightSeat, FlightService flightService, SeatService seatService);

    @Mapping(target = "flight", expression = "java(flightService.getFlightById(dto.getFlightId()).get())")
    @Mapping(target = "seat", expression = "java(seatService.getSeatById(dto.getSeatNumber()))")
    FlightSeat convertToFlightSeatEntity(FlightSeatDTO dto,FlightService flightService,SeatService seatService);

}
