package app.services.interfaces;

import app.entities.Destination;
import app.entities.Flight;
import app.enums.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FlightService {

    List<Flight> getAllFlights();

    Page<Flight> getAllFlights(Pageable pageable);

    Flight getFlightByCode(String code);

    Page<Flight> getAllFlightsByDestinationsAndDates(String cityFrom, String cityTo, String dateStart, String dateFinish, Pageable pageable);

    List<Flight> getFlightsByDestinationsAndDepartureDate(Destination fromId, Destination toId, LocalDate departureDate);

    Flight getFlightByIdAndDates(Long id, String start, String finish);

    Optional<Flight> getFlightById(Long id);

    Flight saveFlight(Flight flight);

    Flight updateFlight(Long id, Flight updated);

    List<Flight> getListDirectFlightsByFromAndToAndDepartureDate(Airport airportCodeFrom, Airport airportCodeTo, Date departureDate);

    List<Flight> getListNonDirectFlightsByFromAndToAndDepartureDate(int airportIdFrom, int airportIdTo, Date departureDate);

    void deleteFlightById(Long id);
}
