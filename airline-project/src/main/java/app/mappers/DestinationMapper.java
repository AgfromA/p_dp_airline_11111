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

    DestinationDto toDto(Destination destination);

    @Mapping(target = "airportName", expression = "java(destinationDto.getAirportCode().getAirportName())")
    @Mapping(target = "cityName", expression = "java(destinationDto.getAirportCode().getCity())")
    @Mapping(target = "countryName", expression = "java(destinationDto.getAirportCode().getCountry())")
    Destination toEntity(DestinationDto destinationDto);

    List<DestinationDto> toDtoList(List<Destination> destinations);

    default List<Destination> toEntityList(List<DestinationDto> destinationDtos) {
        return destinationDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

    }
}