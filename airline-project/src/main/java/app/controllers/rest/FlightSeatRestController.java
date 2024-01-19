package app.controllers.rest;

import app.controllers.api.rest.FlightSeatRestApi;
import app.dto.FlightSeatDto;
import app.mappers.FlightSeatMapper;
import app.services.FlightSeatService;
import app.services.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FlightSeatRestController implements FlightSeatRestApi {

    private final FlightSeatService flightSeatService;
    private final FlightService flightService;
    private final FlightSeatMapper flightSeatMapper;

    @Override
    public ResponseEntity<List<FlightSeatDto>> getAllFlightSeats(Integer page, Integer size) {
        log.info("getAll: get all FlightSeats");
        if (page == null || size == null) {
            log.info("getAll: get all list FlightSeats");
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
        var flightseats = flightSeatService.getAllListFlightSeats();
        if (flightseats.isEmpty()) {
            log.info("getAll: FlightSeats not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll: found {} FlightSeats", flightseats.size());
            return new ResponseEntity<>(flightseats, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<FlightSeatDto>> getAllFlightSeatsFiltered(
            Integer page, Integer size, Long flightId, Boolean isSold, Boolean isRegistered) {
        Pageable pageable = PageRequest.of(page, size);
        List<FlightSeatDto> result = null;
        if (isSold != null && !isSold && isRegistered != null && !isRegistered) {
            log.info("getAll: get not sold and not registered FlightSeats by id={}", flightId);
            result = flightSeatService.getFreeSeatsById(pageable, flightId).getContent();
        } else if (isSold != null && !isSold) {
            log.info("getAll: get not sold FlightSeats by id={}", flightId);
            result = flightSeatService.getNotSoldFlightSeatsById(flightId, pageable).getContent();
        } else if (isRegistered != null && !isRegistered) {
            log.info("getAll: get not registered FlightSeat by id={}", flightId);
            result = flightSeatService.findNotRegisteredFlightSeatsById(flightId, pageable).getContent();
        } else {
            log.info("getAll: get FlightSeats by flightId. flightId={}", flightId);
            result = flightSeatService.getFlightSeatsByFlightId(flightId, pageable).getContent();
        }
        return (result.isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<FlightSeatDto> getFlightSeatById(Long id) {
        log.info("get: FlightSeat by id={}", id);
        return (flightSeatService.getFlightSeatById(id).isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(flightSeatMapper.toDto(flightSeatService.getFlightSeatById(id).get(), flightService));
    }

    @Override
    public ResponseEntity<List<FlightSeatDto>> generateAllFlightSeatsByFlightId(Long flightId) {
        if (flightService.getFlightById(flightId).isEmpty()) {
            log.error("generate: Flight with id = {} not found", flightId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("generate: FlightSeats by flightId. flightId={}", flightId);
        var flightSeats = flightSeatService.getFlightSeatsByFlightId(flightId);
        if (!flightSeats.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(flightSeats), HttpStatus.OK);
        }
        return new ResponseEntity<>(flightSeatService.addFlightSeatsByFlightId(flightId)
                .stream()
                .map(f -> flightSeatMapper.toDto(f, flightService))
                .collect(Collectors.toList()),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<FlightSeatDto> updateFlightSeatById(Long id, FlightSeatDto flightSeatDTO) {
        var flightSeat = flightSeatService.getFlightSeatById(id);
        log.info("update: FlightSeat by id={}", id);
        if (flightSeat.isEmpty()) {
            log.error("update: FlightSeat with id={} doesn't exist.", id);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(flightSeatService.editFlightSeat(id, flightSeatDTO));
    }

    @Override
    public ResponseEntity<String> deleteFlightSeatById(Long id) {
        try {
            flightSeatService.deleteFlightSeatById(id);
            log.info("deleteFlightSeatById: FlightSeat with id={} deleted", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("deleteFlightSeatById: error while deleting - FlightSeat with id={} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<FlightSeatDto> createFlightSeat(FlightSeatDto flightSeat) {
        var savedFlightSeat = flightSeatService.saveFlightSeat(flightSeat);
        log.info("createFlightSeat: FlightSeat saved with id= {}", savedFlightSeat.getId());
        return ResponseEntity.ok(savedFlightSeat);
    }
}