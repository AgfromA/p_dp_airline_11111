package app.mappers;

import app.dto.SeatDTO;
import app.entities.Seat;
import app.services.interfaces.AircraftService;
import app.services.interfaces.CategoryService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface SeatMapper {

    SeatMapper INSTANCE = Mappers.getMapper(SeatMapper.class);

    @Mapping(target = "category", expression = "java(seat.getCategory().getCategoryType())")
    @Mapping(target = "aircraftId", expression = "java(seat.getAircraft().getId())")
    SeatDTO convertToSeatDTOEntity(Seat seat);

    @Mapping(target = "category", expression = "java(categoryService.getCategoryByType(dto.getCategory()))")
    @Mapping(target = "aircraft", expression = "java(aircraftService.getAircraftById(dto.getAircraftId()))")
    Seat convertToSeatEntity(SeatDTO dto, @Context CategoryService categoryService, @Context AircraftService aircraftService);

    default List<SeatDTO> convertToSeatDTOList(List<Seat> seatList) {
        return seatList.stream()
                .map(this::convertToSeatDTOEntity)
                .collect(Collectors.toList());
    }

    default List<Seat> convertToSeatEntityList(List<SeatDTO> seatDTOList, CategoryService categoryService,
                                               AircraftService aircraftService) {
        return seatDTOList.stream()
                .map(seatDTO -> convertToSeatEntity(seatDTO, categoryService, aircraftService))
                .collect(Collectors.toList());
    }

}
