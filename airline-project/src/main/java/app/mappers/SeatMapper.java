package app.mappers;

import app.dto.SeatDto;
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
    SeatDto convertToSeatDtoEntity(Seat seat);

    @Mapping(target = "category", expression = "java(categoryService.getCategoryByType(seatDto.getCategory()))")
    @Mapping(target = "aircraft", expression = "java(aircraftService.getAircraftById(seatDto.getAircraftId()))")
    Seat convertToSeatEntity(SeatDto seatDto,
                             @Context CategoryService categoryService,
                             @Context AircraftService aircraftService);

    default List<SeatDto> convertToSeatList(List<Seat> seatList) {
        return seatList.stream()
                .map(this::convertToSeatDtoEntity)
                .collect(Collectors.toList());
    }

    default List<Seat> convertToSeatEntityList(List<SeatDto> seatDtoList,
                                               CategoryService categoryService,
                                               AircraftService aircraftService) {
        return seatDtoList.stream()
                .map(seatDTO -> convertToSeatEntity(seatDTO, categoryService, aircraftService))
                .collect(Collectors.toList());
    }
}