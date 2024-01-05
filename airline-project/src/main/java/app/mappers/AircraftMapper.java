package app.mappers;

import app.dto.AircraftDTO;
import app.entities.Aircraft;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AircraftMapper {
    Aircraft convertToAircraftEntity(AircraftDTO aircraftDTO);

    AircraftDTO convertToAircarftDTOEntity(Aircraft aircraft);
    List<Aircraft> convertToAircraftEntityList(List<AircraftDTO> aircraftDTOList);
    List<AircraftDTO> convertToAircarftDTOList (List<Aircraft> aircraftList);
}
