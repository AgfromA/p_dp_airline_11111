package app.mappers;

import app.dto.PassengerDTO;
import app.entities.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerMapper {

    PassengerDTO convertToPassengerDTO(Passenger passenger);

    Passenger convertToPassenger(PassengerDTO passengerDTO);

    List<PassengerDTO> convertToPassengerDTOList(List<Passenger> passengerList);

    List<Passenger> convertToPassengerList(List<PassengerDTO> passengerDTOList);
}
