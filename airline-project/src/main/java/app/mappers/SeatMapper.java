package app.mappers;

import app.dto.SeatDTO;
import app.entities.Seat;
import app.services.interfaces.AircraftService;
import app.services.interfaces.CategoryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SeatMapper {

    SeatMapper INSTANCE = Mappers.getMapper(SeatMapper.class);

    @Mapping(target = "category", expression = "java(seat.getCategory().getCategoryType())")
    @Mapping(target = "aircraftId", expression = "java(seat.getAircraft().getId())")
    SeatDTO convertToSeatDTOEntity(Seat seat);

    @Mapping(target = "category", expression = "java(categoryService.getCategoryByType(dto.getCategory()))")
    @Mapping(target = "aircraft", expression = "java(aircraftService.getAircraftById(dto.getAircraftId()))")
    Seat convertToSeatEntity(SeatDTO dto, CategoryService categoryService, AircraftService aircraftService);

}
