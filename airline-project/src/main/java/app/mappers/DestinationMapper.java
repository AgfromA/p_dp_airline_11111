package app.mappers;

import app.dto.DestinationDto;
import app.entities.Destination;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public interface DestinationMapper {

    DestinationDto convertToDestinationDtoEntity(Destination destination);

    @Mapping(target = "airportName", expression = "java(destinationDto.getAirportCode().getAirportName())")
    @Mapping(target = "cityName", expression = "java(destinationDto.getAirportCode().getCity())")
    @Mapping(target = "countryName", expression = "java(destinationDto.getAirportCode().getCountry())")
    Destination convertToDestinationEntity(DestinationDto destinationDto);

    List<DestinationDto> convertToDestinationDtoList(List<Destination> destinations);

    default List<Destination> convertToDestinationEntityList(List<DestinationDto> destinationDtos) {
        return destinationDtos.stream()
                .map(this::convertToDestinationEntity)
                .collect(Collectors.toList());

    }
}