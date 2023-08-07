package app.services;

import app.entities.Destination;
import app.entities.Flight;
import app.repositories.AircraftRepository;
import app.repositories.DestinationRepository;
import app.enums.Airport;
import app.repositories.FlightRepository;
import app.services.interfaces.FlightService;
import app.util.aop.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final AircraftRepository aircraftRepository;
    private final DestinationRepository destinationRepository;

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Page<Flight> getAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Flight getFlightByCode(String code) {
        return flightRepository.getByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Page<Flight> getAllFlightsByDestinationsAndDates(String cityFrom, String cityTo,
                                                        String dateStart, String dateFinish,
                                                        Pageable pageable) {
        return flightRepository.getAllFlightsByDestinationsAndDates(cityFrom, cityTo, dateStart, dateFinish, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getFlightsByDestinationsAndDepartureDate(Destination from, Destination to,
                                                                 LocalDate departureDate) {
        return flightRepository.getByFromAndToAndDepartureDate(from, to, departureDate);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getListDirectFlightsByFromAndToAndDepartureDate(Airport airportCodeFrom, Airport airportCodeTo, Date departureDate) {
        return flightRepository.getListDirectFlightsByFromAndToAndDepartureDate(airportCodeFrom, airportCodeTo, departureDate);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getListNonDirectFlightsByFromAndToAndDepartureDate(int airportIdFrom, int airportIdTo, Date departureDate) {
        return flightRepository.getListNonDirectFlightsByFromAndToAndDepartureDate(airportIdFrom, airportIdTo, departureDate);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Flight getFlightByIdAndDates(Long id, String start, String finish) {
        var flight = flightRepository.findById(id);
        if (flight.isPresent() && (flight.get().getDepartureDateTime().isEqual(LocalDateTime.parse(start))
                    && flight.get().getArrivalDateTime().isEqual(LocalDateTime.parse(finish)))) {
                return flight.get();
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }


    @Override
    @Loggable
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    @Loggable
    public Flight updateFlight(Long id, Flight updated) {
        updated.setId(id);
        if (updated.getAircraft() == null) {
            updated.setAircraft(getFlightById(id).get().getAircraft());
        } else {
            updated.setAircraft(aircraftRepository.findByAircraftNumber(updated.getAircraft().getAircraftNumber()));
        }
        if (updated.getFrom() == null) {
            updated.setFrom(getFlightById(id).get().getFrom());
        } else {
            updated.setFrom(destinationRepository.findDestinationByAirportCode(updated.getFrom().getAirportCode()).orElse(null));
        }
        if (updated.getTo() == null) {
            updated.setTo(getFlightById(id).get().getTo());
        } else {
            updated.setTo(destinationRepository.findDestinationByAirportCode(updated.getTo().getAirportCode()).orElse(null));
        }
        return flightRepository.saveAndFlush(updated);
    }

    @Override
    @Loggable
    public void deleteFlightById(Long id) {
        flightRepository.deleteById(id);
    }
}