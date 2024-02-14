package app.services;

import app.dto.FlightDto;
import app.entities.Flight;
import app.enums.FlightStatus;
import app.exceptions.EntityNotFoundException;
import app.mappers.FlightMapper;
import app.repositories.*;
import app.enums.Airport;
import lombok.RequiredArgsConstructor;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class FlightService {

    @Lazy // FIXME костыль
    @Autowired
    private FlightSeatService flightSeatService;
    @Lazy // FIXME костыль
    @Autowired
    private TicketService ticketService;
    private final FlightRepository flightRepository;
    private final AircraftService aircraftService;
    private final DestinationService destinationService;
    private final FlightMapper flightMapper;
    private static final Pattern LAT_LONG_PATTERN = Pattern.compile("([-+]?\\d{1,2}\\.\\d+),\\s+([-+]?\\d{1,3}\\.\\d+)");

    public List<FlightDto> getAllFlights() {
        var flights = flightRepository.findAll();
        return flightMapper.toDtoList(flights, this);
    }

    public Page<FlightDto> getAllFlights(Integer page, Integer size) {
        return flightRepository.findAll(PageRequest.of(page, size))
                .map(flight -> flightMapper.toDto(flight, this));
    }

    public List<Flight> getListDirectFlightsByFromAndToAndDepartureDate(Airport airportCodeFrom, Airport airportCodeTo, Date departureDate) {
        return flightRepository.getListDirectFlightsByFromAndToAndDepartureDate(airportCodeFrom, airportCodeTo, departureDate);
    }

    public Optional<Flight> getFlight(Long id) {
        return flightRepository.findById(id);
    }

    public Optional<FlightDto> getFlightDto(Long id) {
        return flightRepository.findById(id).map(flight -> flightMapper.toDto(flight, this));
    }

    public FlightDto createFlight(FlightDto flightDto) {
        // FIXME заменить на аннотацию в ДТО
        flightDto.setSeats(null);
        flightDto.setFlightStatus(FlightStatus.ON_TIME);
        aircraftService.checkIfAircraftExists(flightDto.getAircraftId());

        var flight = flightMapper.toEntity(flightDto, aircraftService, destinationService, ticketService, flightSeatService);
        var savedFlight = flightRepository.save(flight);
        return flightMapper.toDto(savedFlight, this);
    }

    public FlightDto updateFlight(Long id, FlightDto flightDto) {
        var flight = checkIfFlightExists(id);
        if (flightDto.getCode() != null) {
            flight.setCode(flightDto.getCode());
        }
        if (flightDto.getAirportFrom() != null) {
            flight.setFrom(destinationService.getDestinationByAirportCode(flightDto.getAirportFrom()));
        }
        if (flightDto.getAirportTo() != null) {
            flight.setTo(destinationService.getDestinationByAirportCode(flightDto.getAirportTo()));
        }
        if (flightDto.getDepartureDateTime() != null) {
            flight.setDepartureDateTime(flightDto.getDepartureDateTime());
        }
        if (flightDto.getArrivalDateTime() != null) {
            flight.setArrivalDateTime(flightDto.getArrivalDateTime());
        }
        if (flightDto.getAircraftId() != null) {
            flight.setAircraft(aircraftService.checkIfAircraftExists(flightDto.getAircraftId()));
        }
        if (flightDto.getFlightStatus() != null) {
            flight.setFlightStatus(flightDto.getFlightStatus());
        }
        var updatedFlight = flightRepository.save(flight);
        return flightMapper.toDto(updatedFlight, this);
    }

    public void deleteFlightById(Long id) {
        checkIfFlightExists(id);
        flightRepository.deleteById(id);
    }

    public Long getDistance(Flight flight) {
        Geodesic geodesic = Geodesic.WGS84;
        GeodesicData calculate = geodesic.Inverse(
                parseLatitude(flight.getFrom().getAirportCode()),
                parseLongitude(flight.getFrom().getAirportCode()),
                parseLatitude(flight.getTo().getAirportCode()),
                parseLongitude(flight.getTo().getAirportCode())
        );
        return (long) (calculate.s12 / 1000);
    }

    public double parseLatitude(Airport airport) {
        Matcher matcher = LAT_LONG_PATTERN.matcher(airport.getCoordinates());
        matcher.find();
        return Double.parseDouble(matcher.group(1));
    }

    public double parseLongitude(Airport airport) {
        Matcher matcher = LAT_LONG_PATTERN.matcher(airport.getCoordinates());
        matcher.find();
        return Double.parseDouble(matcher.group(2));
    }

    public Flight checkIfFlightExists(Long flightId) {
        return getFlight(flightId).orElseThrow(
                () -> new EntityNotFoundException("Operation was not finished because Flight was not found with id = " + flightId)
        );
    }
}