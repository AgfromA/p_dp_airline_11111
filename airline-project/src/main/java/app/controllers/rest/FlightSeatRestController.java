package app.controllers.rest;

import app.controllers.api.rest.FlightSeatRestApi;
import app.dto.FlightSeatDto;
import app.services.FlightSeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FlightSeatRestController implements FlightSeatRestApi {

    private final FlightSeatService flightSeatService;

    @Override
    public ResponseEntity<List<FlightSeatDto>> getAllFlightSeats(Integer page, Integer size) {
        log.info("getAll:");
        if (page == null || size == null) {
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var flightSeats = flightSeatService.getAllFlightSeats(page, size);
        return flightSeats.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(flightSeats.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<FlightSeatDto>> createUnPagedResponse() {
        var flightSeats = flightSeatService.getAllFlightSeats();
        if (flightSeats.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAllFlightSeats: count {}", flightSeats.size());
            return new ResponseEntity<>(flightSeats, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<FlightSeatDto>> getAllFlightSeatsFiltered(
            Integer page, Integer size, Long flightId, Boolean isSold, Boolean isRegistered) {
        Pageable pageable = PageRequest.of(page, size);
        List<FlightSeatDto> result = null;
        if (Boolean.FALSE.equals(isSold) && Boolean.FALSE.equals(isRegistered)) {
            log.info("getAllFlightSeatsFiltered: not sold and not registered by flightId={}", flightId);
            result = flightSeatService.getFreeSeatsById(pageable, flightId).getContent();
        } else if (Boolean.FALSE.equals(isSold)) {
            log.info("getAllFlightSeatsFiltered: not sold by flightId={}", flightId);
            result = flightSeatService.getNotSoldFlightSeatsById(flightId, pageable).getContent();
        } else if (Boolean.FALSE.equals(isRegistered)) {
            log.info("getAllFlightSeatsFiltered: not registered by flightId={}", flightId);
            result = flightSeatService.findNotRegisteredFlightSeatsById(flightId, pageable).getContent();
        } else {
            log.info("getAllFlightSeatsFiltered: by flightId={}", flightId);
            result = flightSeatService.getFlightSeatsByFlightId(flightId, pageable).getContent();
        }
        return (result.isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<FlightSeatDto> getFlightSeat(Long id) {
        log.info("getFlightSeatById: by id={}", id);
        var flightSeat = flightSeatService.getFlightSeatDtoById(id);
        return flightSeat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<FlightSeatDto> createFlightSeat(FlightSeatDto flightSeat) {
        var savedFlightSeat = flightSeatService.createFlightSeat(flightSeat);
        log.info("createFlightSeat:");
        return ResponseEntity.ok(savedFlightSeat);
    }

    @Override
    public ResponseEntity<FlightSeatDto> updateFlightSeat(Long id, FlightSeatDto flightSeatDTO) {
        log.info("updateFlightSeat: by id={}", id);
        return ResponseEntity.ok(flightSeatService.editFlightSeat(id, flightSeatDTO));
    }

    @Override
    public ResponseEntity<String> deleteFlightSeat(Long id) {
        log.info("deleteFlightSeat: by id={}", id);
        flightSeatService.deleteFlightSeatById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<FlightSeatDto>> generateFlightSeats(Long flightId) {
        log.info("generateFlightSeats: by flightId={}", flightId);
        return new ResponseEntity<>(flightSeatService.generateFlightSeats(flightId), HttpStatus.CREATED);
    }
}