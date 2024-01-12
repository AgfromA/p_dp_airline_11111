package app.services;

import app.dto.FlightDto;
import app.entities.Destination;
import app.entities.Flight;
import app.mappers.FlightMapper;
import app.repositories.*;
import app.enums.Airport;
import app.util.aop.Loggable;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class FlightService {

    private final FlightRepository flightRepository;
    private final AircraftService aircraftService;
    private final DestinationService destinationService;
    private final FlightSeatService flightSeatService;
    private final TicketService ticketService;
    private final FlightMapper flightMapper;
    private final Pattern LAT_LONG_PATTERN = Pattern.compile("([-+]?\\d{1,2}\\.\\d+),\\s+([-+]?\\d{1,3}\\.\\d+)");

    public FlightService(FlightRepository flightRepository, AircraftService aircraftService,
                         DestinationService destinationService, @Lazy FlightSeatService flightSeatService,
                         @Lazy TicketService ticketService, FlightMapper flightMapper, SeatService seatService) {
        this.flightRepository = flightRepository;
        this.aircraftService = aircraftService;
        this.destinationService = destinationService;
        this.flightSeatService = flightSeatService;
        this.ticketService = ticketService;
        this.flightMapper = flightMapper;
    }

    @Transactional(readOnly = true)
    @Loggable
    public List<FlightDto> getAllListFlights() {
        return flightMapper.convertFlightListToFlighDtotList(flightRepository.findAll(), this);
    }

    @Transactional(readOnly = true)
    @Loggable
    public Page<FlightDto> getAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable).map(flight -> flightMapper.flightToFlightDto(flight, this));
    }

    @Transactional(readOnly = true)
    @Loggable
    public Flight getFlightByCode(String code) {
        return flightRepository.getByCode(code);
    }

    @Transactional(readOnly = true)
    @Loggable
    public Page<FlightDto> getAllFlightsByDestinationsAndDates(String cityFrom, String cityTo,
                                                               String dateStart, String dateFinish,
                                                               Pageable pageable) {
        return flightRepository.getAllFlightsByDestinationsAndDates(cityFrom, cityTo, dateStart, dateFinish, pageable)
                .map(flight -> flightMapper.flightToFlightDto(flight, this));
    }

    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getFlightsByDestinationsAndDepartureDate(Destination from, Destination to,
                                                                 LocalDate departureDate) {
        return flightRepository.getByFromAndToAndDepartureDate(from, to, departureDate);
    }

    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getListDirectFlightsByFromAndToAndDepartureDate(Airport airportCodeFrom, Airport airportCodeTo
            , Date departureDate) {
        return flightRepository.getListDirectFlightsByFromAndToAndDepartureDate(airportCodeFrom, airportCodeTo
                , departureDate);
    }

    @Transactional(readOnly = true)
    @Loggable
    public List<Flight> getListNonDirectFlightsByFromAndToAndDepartureDate(int airportIdFrom, int airportIdTo, Date departureDate) {
        return flightRepository.getListNonDirectFlightsByFromAndToAndDepartureDate(airportIdFrom, airportIdTo, departureDate);
    }

    @Transactional(readOnly = true)
    @Loggable
    public FlightDto getFlightByIdAndDates(Long id, String start, String finish) {
        var flight = flightRepository.findById(id);
        if (flight.isPresent() && (flight.get().getDepartureDateTime().isEqual(LocalDateTime.parse(start))
                && flight.get().getArrivalDateTime().isEqual(LocalDateTime.parse(finish)))) {
            return flightMapper.flightToFlightDto(flight.get(), this);
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Loggable
    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public FlightDto saveFlight(FlightDto flightDto) {
        var savedFlight = flightRepository.save(flightMapper.flightDtotoFlight(flightDto, aircraftService,
                destinationService, ticketService, flightSeatService));
        return flightMapper.flightToFlightDto(savedFlight, this);
    }

    @Loggable
    public FlightDto updateFlight(Long id, FlightDto flightDto) {
        var updatedFlight = flightRepository.saveAndFlush(flightMapper.flightDtotoFlight(flightDto, aircraftService,
                destinationService, ticketService, flightSeatService));
        return flightMapper.flightToFlightDto(updatedFlight, this);
    }

    @Loggable
    public void deleteFlightById(Long id) {
        flightRepository.deleteById(id);
    }

    @Loggable
    public Long getDistance(Flight flight) {
        Geodesic geodesic = Geodesic.WGS84;
        GeodesicData calculate = geodesic.Inverse(
                parseLatitude(flight.getFrom().getAirportCode())
                , parseLongitude(flight.getFrom().getAirportCode())
                , parseLatitude(flight.getTo().getAirportCode())
                , parseLongitude(flight.getTo().getAirportCode())
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
}