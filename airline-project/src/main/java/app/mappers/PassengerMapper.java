package app.mappers;

import app.dto.PassengerDTO;
import app.entities.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerMapper {

    PassengerDTO convertToPassengerDTO(Passenger passenger);

    Passenger convertToPassenger(PassengerDTO passengerDTO);
}
