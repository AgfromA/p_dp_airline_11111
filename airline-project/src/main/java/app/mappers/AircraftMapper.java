package app.mappers;

import app.dto.AircraftDto;
import app.entities.Aircraft;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AircraftMapper {

    Aircraft toEntity(AircraftDto aircraftDTO);

    AircraftDto toDto(Aircraft aircraft);

    List<Aircraft> toEntityList(List<AircraftDto> aircraftDtoList);

    List<AircraftDto> toDtoList(List<Aircraft> aircraftList);
}