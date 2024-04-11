package app.repositories;

import app.entities.Flight;
import app.enums.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query(value = "select f\n" +
            "from Flight f\n" +
            "join Destination d on f.from.id = d.id\n" +
            "join Destination d2 on f.to.id = d2.id\n" +
            "where d.airportCode = ?1 AND d2.airportCode = ?2 AND cast(f.departureDateTime as date) = ?3")
    List<Flight> getListDirectFlightsByFromAndToAndDepartureDate(Airport airportCodeFrom, Airport airportCodeTo, Date departureDate);

    void deleteById(Long id);

    @Query(value = "SELECT f FROM Flight f WHERE f.from.id = :id OR f.to.id = :id")
    List<Flight> findByDestinationId(Long id);
}