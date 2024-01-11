package app.controllers.rest;

import app.controllers.api.rest.FlightRestApi;
import app.dto.FlightDTO;
import app.enums.FlightStatus;
import app.mappers.FlightMapper;
import app.services.interfaces.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<List<FlightDTO>> getAllPagesFlightsByDestinationsAndDates(Integer page, Integer size,
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
        Page<FlightDTO> flightDTOPage;
        Pageable pageable = PageRequest.of(page, size);

        if (cityFrom == null && cityTo == null && dateStart == null && dateFinish == null) {
            flightDTOPage = flightService.getAllFlights(pageable);
            log.info("get all Flights by page");
        } else {
            flightDTOPage = flightService
                    .getAllFlightsByDestinationsAndDates(cityFrom, cityTo, dateStart, dateFinish, pageable);
            log.info("getAllFlightsByDestinationsAndDates: get all Flights or Flights by params");
        }
        return flightDTOPage.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(flightDTOPage.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<FlightDTO>> createUnPagedResponse() {
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
    public ResponseEntity<FlightDTO> getFlightDTOById(Long id) {
        log.info("getById: get Flight by id. id = {}", id);
        var flight = flightService.getFlightById(id);
        return flight.isPresent()
                ? new ResponseEntity<>(flightMapper.flightToFlightDTO(flight.get(), flightService), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<FlightDTO> getFlightDTOByIdAndDates(Long id, String start, String finish) {
        log.info("getByIdAndDates: get Flight by id={} and dates from {} to {}", id, start, finish);
        var flight = flightService.getFlightByIdAndDates(id, start, finish);
        return flight != null
                ? new ResponseEntity<>(flight, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<FlightStatus[]> getAllFlightStatus() {
        log.info("getAllFlightStatus: get all Flight Statuses");
        return new ResponseEntity<>(flightService.getAllListFlights().stream()
                .map(FlightDTO::getFlightStatus)
                .distinct().collect(Collectors.toList()).toArray(FlightStatus[]::new), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FlightDTO> createFlight(FlightDTO flightDTO) {
        log.info("create: create new Flight");
        return new ResponseEntity<>(flightService.saveFlight(flightDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<FlightDTO> updateFlightById(Long id, FlightDTO flightDTO) {
        var flight = flightService.getFlightById(id);
        if (flight.isEmpty()) {
            log.error("update: Flight with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        flightDTO.setId(id);
        log.info("update: Flight with id = {} updated", id);
        return new ResponseEntity<>(flightService.updateFlight(id, flightDTO), HttpStatus.OK);
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