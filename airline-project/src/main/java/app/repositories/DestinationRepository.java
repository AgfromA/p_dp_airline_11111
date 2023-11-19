package app.repositories;

import app.entities.Destination;
import app.enums.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

    List<Destination> findAll();

    Page<Destination> findAll(Pageable pageable);

    Page<Destination> findByCityNameContainingIgnoreCase(Pageable pageable, String name);

    Page<Destination> findByCountryNameContainingIgnoreCase(Pageable pageable, String name);

    Page<Destination> findByTimezoneContainingIgnoreCase(Pageable pageable, String timezone);

    Destination getDestinationByAirportCode(Airport airportCode);

    Optional<Destination> findDestinationByAirportCode(Airport airportCode);
}
