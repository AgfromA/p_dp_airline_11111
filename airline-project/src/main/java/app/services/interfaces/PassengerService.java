package app.services.interfaces;

import app.dto.PassengerDTO;
import app.entities.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PassengerService {
    Passenger savePassenger(PassengerDTO passengerDTO);

    Passenger updatePassengerById(Long id, PassengerDTO passengerDTO);

    Page<PassengerDTO> getAllPagesPassengerByKeyword(Pageable pageable, String firstName, String lastName, String email, String serialNumberPassport);

    Optional<Passenger> getPassengerById(Long id);

    void deletePassengerById(Long id);

    Page<PassengerDTO> getAllPagesPassengers(Pageable pageable);

    Page<PassengerDTO> getAllPagesPassengers(int page, int size);
}