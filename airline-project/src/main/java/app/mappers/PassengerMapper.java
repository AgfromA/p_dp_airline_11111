package app.mappers;

import app.dto.PassengerDto;
import app.entities.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerMapper {

    PassengerDto toDto(Passenger passenger);

    Passenger toEntity(PassengerDto passengerDTO);

    List<PassengerDto> toDtoList(List<Passenger> passengerList);

    List<Passenger> toEntityList(List<PassengerDto> passengerDtoList);
}