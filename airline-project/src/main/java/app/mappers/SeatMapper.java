package app.mappers;

import app.dto.SeatDto;
import app.entities.Seat;
import app.services.AircraftService;
import app.services.CategoryService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeatMapper {

    @Mapping(target = "category", expression = "java(seat.getCategory().getCategoryType())")
    @Mapping(target = "aircraftId", expression = "java(seat.getAircraft().getId())")
    SeatDto toDto(Seat seat);

    @Mapping(target = "category", expression = "java(categoryService.getCategoryByType(seatDto.getCategory()))")
    @Mapping(target = "aircraft", expression = "java(aircraftService.getAircraft(seatDto.getAircraftId()))")
    Seat toEntity(SeatDto seatDto,
                  @Context CategoryService categoryService,
                  @Context AircraftService aircraftService);

    default List<SeatDto> toDtoList(List<Seat> seatList) {
        return seatList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    default List<Seat> toEntityList(List<SeatDto> seatDtoList,
                                    CategoryService categoryService,
                                    AircraftService aircraftService) {
        return seatDtoList.stream()
                .map(seatDTO -> toEntity(seatDTO, categoryService, aircraftService))
                .collect(Collectors.toList());
    }
}