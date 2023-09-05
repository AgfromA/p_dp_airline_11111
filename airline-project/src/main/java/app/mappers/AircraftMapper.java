package app.mappers;

import app.dto.AircraftDTO;
import app.entities.Aircraft;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AircraftMapper {
    Aircraft convertToAircraftEntity(AircraftDTO aircraftDTO);

    AircraftDTO convertToAircarftDTOEntity(Aircraft aircraft);
}
