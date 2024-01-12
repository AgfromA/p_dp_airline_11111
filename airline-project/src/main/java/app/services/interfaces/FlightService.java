package app.services.interfaces;

import app.dto.FlightDto;
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

    List<FlightDto> getAllListFlights();

    Page<FlightDto> getAllFlights(Pageable pageable);

    Flight getFlightByCode(String code);

    Page<FlightDto> getAllFlightsByDestinationsAndDates(String cityFrom, String cityTo, String dateStart,
                                                        String dateFinish, Pageable pageable);

    List<Flight> getFlightsByDestinationsAndDepartureDate(Destination fromId, Destination toId, LocalDate departureDate);

    FlightDto getFlightByIdAndDates(Long id, String start, String finish);

    Optional<Flight> getFlightById(Long id);

    FlightDto saveFlight(FlightDto flightDTO);

    FlightDto updateFlight(Long id, FlightDto flightDTO);

    List<Flight> getListDirectFlightsByFromAndToAndDepartureDate(Airport airportCodeFrom, Airport airportCodeTo,
                                                                 Date departureDate);

    List<Flight> getListNonDirectFlightsByFromAndToAndDepartureDate(int airportIdFrom, int airportIdTo,
                                                                    Date departureDate);

    void deleteFlightById(Long id);

    Long getDistance(Flight flight);

    public double parseLatitude(Airport airport);

    public double parseLongitude(Airport airport);
}