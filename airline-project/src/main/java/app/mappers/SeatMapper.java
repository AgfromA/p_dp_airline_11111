package app.mappers;

import app.dto.SeatDto;
import app.entities.Seat;
import app.services.AircraftService;
import app.services.CategoryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class SeatMapper {
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    protected AircraftService aircraftService;

    @Mapping(target = "category", expression = "java(seat.getCategory().getCategoryType())")
    @Mapping(target = "aircraftId", expression = "java(seat.getAircraft().getId())")
    public abstract SeatDto toDto(Seat seat);

    @Mapping(target = "category", expression = "java(categoryService.getCategoryByType(seatDto.getCategory()))")
    @Mapping(target = "aircraft", expression = "java(aircraftService.getAircraft(seatDto.getAircraftId()))")
    public abstract Seat toEntity(SeatDto seatDto);

    public List<SeatDto> toDtoList(List<Seat> seatList) {
        return seatList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Seat> toEntityList(List<SeatDto> seatDtoList) {
        return seatDtoList.stream()
                .map(seatDTO -> toEntity(seatDTO))
                .collect(Collectors.toList());
    }
}