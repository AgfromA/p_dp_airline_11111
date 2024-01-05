package app.mappers;

import app.dto.DestinationDTO;
import app.entities.Destination;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public interface DestinationMapper {

    DestinationDTO convertToDestinationDTOEntity(Destination destination);

    @Mapping(target = "airportName", expression = "java(dto.getAirportCode().getAirportName())")
    @Mapping(target = "cityName", expression = "java(dto.getAirportCode().getCity())")
    @Mapping(target = "countryName", expression = "java(dto.getAirportCode().getCountry())")
    Destination convertToDestinationEntity(DestinationDTO dto);

    List<DestinationDTO> convertToDestinationDTOList(List<Destination> destinations);

    default List<Destination> convertToDestinationEntityList(List<DestinationDTO> destinationDTOS) {
        return destinationDTOS.stream()
                .map(this::convertToDestinationEntity)
                .collect(Collectors.toList());

    }

}