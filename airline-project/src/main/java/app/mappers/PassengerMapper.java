package app.mappers;

import app.dto.PassengerDto;
import app.entities.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerMapper {

    PassengerDto convertToPassengerDto(Passenger passenger);

    Passenger convertToPassenger(PassengerDto passengerDTO);

    List<PassengerDto> convertToPassengerDtoList(List<Passenger> passengerList);

    List<Passenger> convertToPassengerList(List<PassengerDto> passengerDtoList);
}