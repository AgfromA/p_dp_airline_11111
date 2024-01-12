package app.mappers;

import app.dto.AircraftDto;
import app.entities.Aircraft;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AircraftMapper {

    Aircraft convertToAircraftEntity(AircraftDto aircraftDTO);

    AircraftDto convertToAircarftDTOEntity(Aircraft aircraft);

    List<Aircraft> convertToAircraftEntityList(List<AircraftDto> aircraftDtoList);

    List<AircraftDto> convertToAircarftDTOList(List<Aircraft> aircraftList);
}