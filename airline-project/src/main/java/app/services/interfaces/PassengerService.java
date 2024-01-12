package app.services.interfaces;

import app.dto.PassengerDto;
import app.entities.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PassengerService {
    List<PassengerDto> getAllPassengers();

    Passenger savePassenger(PassengerDto passengerDTO);

    Passenger updatePassengerById(Long id, PassengerDto passengerDTO);

    Page<PassengerDto> getAllPagesPassengerByKeyword(Pageable pageable, String firstName, String lastName, String email, String serialNumberPassport);

    Optional<Passenger> getPassengerById(Long id);

    void deletePassengerById(Long id);

    Page<PassengerDto> getAllPagesPassengers(Pageable pageable);

}