package app.controllers.rest;

import app.controllers.api.rest.FlightRestApi;
import app.dto.FlightDTO;
import app.entities.Flight;
import app.enums.FlightStatus;
import app.services.interfaces.FlightService;
import app.util.mappers.FlightMapper;
import app.util.mappers.FlightSeatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FlightRestController implements FlightRestApi {

    private final FlightService flightService;
    private final FlightMapper flightMapper;

    @Override
    public ResponseEntity<Page<FlightDTO>> getAllPagesFlightsByDestinationsAndDates(
            @RequestParam(required = false) String cityFrom,
            @RequestParam(required = false) String cityTo,
            @RequestParam(required = false) String dateStart,
            @RequestParam(required = false) String dateFinish,
            Pageable pageable) {

            var flightsByParams = flightService
                    .getAllFlightsByDestinationsAndDates(cityFrom, cityTo, dateStart, dateFinish, pageable)
                    .map(flightMapper::convertToFlightDTOEntity);
            log.info("getAllFlightsByDestinationsAndDates: get all Flights or Flights by params");
            return flightsByParams.isEmpty()
                    ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(flightsByParams, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FlightDTO> getFlightDTOById(Long id) {
        log.info("getById: get Flight by id. id = {}", id);
        var flight = flightService.getFlightById(id);
        return flight.isPresent()
                ? new ResponseEntity<>(new FlightDTO(flight.get()), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<FlightDTO> getFlightDTOByIdAndDates(Long id, String start, String finish) {
        log.info("getByIdAndDates: get Flight by id={} and dates from {} to {}", id, start, finish);
        var flight = flightService.getFlightByIdAndDates(id, start, finish);
        return flight != null
                ? new ResponseEntity<>(new FlightDTO(flight), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<FlightStatus[]> getAllFlightStatus() {
        log.info("getAllFlightStatus: get all Flight Statuses");
        return new ResponseEntity<>(flightService.getAllFlights().stream().map(FlightDTO::new)
                .map(FlightDTO::getFlightStatus)
                .distinct().collect(Collectors.toList()).toArray(FlightStatus[]::new), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Flight> createFlight(FlightDTO flightDTO) {
        log.info("create: create new Flight");
        return new ResponseEntity<>(flightService.saveFlight(flightMapper.convertToFlightEntity(flightDTO)),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Flight> updateFlightById(Long id, FlightDTO flightDTO) {
        var flight = flightService.getFlightById(id);
        if (flight.isEmpty()) {
            log.error("update: Flight with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("update: Flight with id = {} updated", id);
        return new ResponseEntity<>(flightService.updateFlight(id, flightMapper.convertToFlightEntity(flightDTO)),
                HttpStatus.OK);
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
}