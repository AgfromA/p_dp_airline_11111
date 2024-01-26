package app.controllers.rest;

import app.controllers.api.rest.FlightRestApi;
import app.dto.FlightDto;
import app.enums.FlightStatus;
import app.services.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FlightRestController implements FlightRestApi {

    private final FlightService flightService;

    @Override
    public ResponseEntity<List<FlightDto>> getAllFlights(Integer page, Integer size) {
        log.info("getAllFlights:");
        if (page == null || size == null) {
            return createUnPagedResponse();
        }
        var flights = flightService.getAllFlights(page, size).getContent();
        return flights.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(flights, HttpStatus.OK);
    }

    private ResponseEntity<List<FlightDto>> createUnPagedResponse() {
        var flights = flightService.getAllFlights();
        if (flights.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAllFlights: count {}", flights.size());
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<FlightDto> getFlight(Long id) {
        log.info("getFlight: by id: {}", id);
        var flightSeat = flightService.getFlightDto(id);
        return flightSeat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<FlightDto> createFlight(FlightDto flightDto) {
        log.info("createFlight:");
        return new ResponseEntity<>(flightService.createFlight(flightDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<FlightDto> updateFlight(Long id, FlightDto flightDto) {
        log.info("updateFlight: by id: {}", id);
        return ResponseEntity.ok(flightService.updateFlight(id, flightDto));
    }

    @Override
    public ResponseEntity<HttpStatus> deleteFlight(Long id) {
        log.info("deleteFlight: by id: {}", id);
        flightService.deleteFlightById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<FlightStatus[]> getAllFlightStatus() {
        log.info("getAllFlightStatus:");
        return ResponseEntity.ok(FlightStatus.values());
    }
}