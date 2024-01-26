package app.controllers.rest;

import app.controllers.api.rest.FlightRestApi;
import app.dto.FlightDto;
import app.enums.FlightStatus;
import app.mappers.FlightMapper;
import app.services.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FlightRestController implements FlightRestApi {

    private final FlightService flightService;
    private final FlightMapper flightMapper;

    @Override
    public ResponseEntity<List<FlightDto>> getAllFlightsByDestinationsAndDates(Integer page, Integer size,
                                                                               String cityFrom, String cityTo,
                                                                               String dateStart, String dateFinish) {
        log.info("get all Flights");
        if (page == null || size == null) {
            log.info("get all List Flights");
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            log.info("no correct data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<FlightDto> flights = null;
        Pageable pageable = PageRequest.of(page, size);

        if (cityFrom == null && cityTo == null && dateStart == null && dateFinish == null) {
            flights = flightService.getAllFlights(pageable).getContent();
            log.info("get all Flights by page");
        } else {
            flights = flightService
                    .getAllFlightsByDestinationsAndDates(cityFrom, cityTo, dateStart, dateFinish, pageable).getContent();
            log.info("getAllFlightsByDestinationsAndDates: get all Flights or Flights by params");
        }
        return flights.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(flights, HttpStatus.OK);
    }

    private ResponseEntity<List<FlightDto>> createUnPagedResponse() {
        var flights = flightService.getAllListFlights();
        if (flights.isEmpty()) {
            log.info("Flights not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("found {} Flights ", flights.size());
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<FlightDto> getFlightById(Long id) {
        log.info("getById: get Flight by id. id = {}", id);
        var flight = flightService.getFlightById(id);
        return flight.isPresent()
                ? new ResponseEntity<>(flightMapper.toDto(flight.get(), flightService), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<FlightStatus[]> getAllFlightStatus() {
        log.info("getAllFlightStatus: get all Flight Statuses");
        return new ResponseEntity<>(flightService.getAllListFlights().stream()
                .map(FlightDto::getFlightStatus)
                .distinct().collect(Collectors.toList()).toArray(FlightStatus[]::new), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FlightDto> createFlight(FlightDto flightDto) {
        log.info("create: create new Flight");
        return new ResponseEntity<>(flightService.saveFlight(flightDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<FlightDto> updateFlightById(Long id, FlightDto flightDto) {
        var flight = flightService.getFlightById(id);
        if (flight.isEmpty()) {
            log.error("update: Flight with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        flightDto.setId(id);
        log.info("update: Flight with id = {} updated", id);
        return new ResponseEntity<>(flightService.updateFlight(id, flightDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteFlightById(Long id) {
        log.info("deleteAircraftById: Flight with id = {}", id);
        try {
            flightService.deleteFlightById(id);
        } catch (Exception e) {
            log.error("deleteAircraftById: error of deleting - Flight with id = {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<FlightDto>> getAllFlightsDTO(Integer page, Integer size) {
        log.info("get all Flights");
        if (page == null || size == null) {
            log.info("get all List Flights");
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            log.info("no correct data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Pageable pageable = PageRequest.of(page, size);
        var flights = flightService.getAllFlights(pageable).getContent();
        return flights.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(flights, HttpStatus.OK);

    }
}