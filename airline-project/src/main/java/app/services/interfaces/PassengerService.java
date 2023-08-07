package app.services.interfaces;

import app.entities.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PassengerService {
    Passenger savePassenger(Passenger passenger);

    Passenger updatePassengerById(Long id, Passenger passenger);

    Page<Passenger> getAllPagesPassengerByKeyword(Pageable pageable, String firstName, String lastName, String email, String serialNumberPassport);

    Optional<Passenger> getPassengerById(Long id);

    void deletePassengerById(Long id);

    Page<Passenger> getAllPagesPassengers(Pageable pageable);

    Page<Passenger> getAllPagesPassengers(int page, int size);
}