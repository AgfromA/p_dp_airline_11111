package app.controllers.rest;

import app.controllers.api.rest.FlightSeatRestApi;
import app.dto.FlightSeatDTO;
import app.dto.SeatDTO;
import app.enums.CategoryType;
import app.mappers.FlightSeatMapper;
import app.services.FlightSeatServiceImpl;
import app.services.interfaces.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    private final FlightSeatServiceImpl flightSeatService;
    private final FlightService flightService;

    @Override
    public ResponseEntity<List<FlightSeatDTO>> getAllPagesFlightSeatsDTO(Integer page, Integer size,
                                                                         Optional<Long> flightId,
                                                                         Boolean isSold,
                                                                         Boolean isRegistered) {
        log.info("get all FlightSeats");
        if (page == null || size == null) {
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            log.info("no correct data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Page<FlightSeatDTO> seats;
        Pageable pageable = PageRequest.of(page, size);

        if (flightId.isEmpty() && isSold == null && isRegistered == null) {
            seats = flightSeatService.getAllFlightSeats(page, size);
            log.info("getAll: found {} Flight Seats", seats.getSize());
        } else if (isSold != null && !isSold && isRegistered != null && !isRegistered) {
            log.info("getAll: get not sold and not registered FlightSeats by id={}", flightId);
            seats = flightSeatService.getFreeSeatsById(pageable, flightId.orElse(null));
        } else if (isSold != null && !isSold) {
            log.info("getAll: get not sold FlightSeats by id={}", flightId);
            seats = flightSeatService.getNotSoldFlightSeatsById(flightId.orElse(null), pageable);
        } else if (isRegistered != null && !isRegistered) {
            log.info("getAll: get not registered FlightSeat by id={}", flightId);
            seats = flightSeatService.findNotRegisteredFlightSeatsById(flightId.orElse(null), pageable);
        } else {
            log.info("getAll: get FlightSeats by flightId. flightId={}", flightId);
            seats = flightSeatService.getFlightSeatsByFlightId(flightId.orElse(null), pageable);
        }
        return seats.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(seats.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<FlightSeatDTO>> createUnPagedResponse() {
        var seats = flightSeatService.getAllListFlightSeats();
        if (!seats.isEmpty()) {
            log.info("getAll List : found {} Flight Seats", seats.size());
            return new ResponseEntity<>(seats, HttpStatus.OK);
        } else {
            log.info("getAll List: Flight Seats not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public ResponseEntity<List<FlightSeatDTO>> getPagesFreeSeatsById(Pageable pageable, Long id) {
        log.info("getFreeSeats: get free seats on Flight with id = {}", id);
        var seats = flightSeatService.getFreeSeatsById(pageable, id);
        return ResponseEntity.ok(seats.getContent());
    }

    @Override
    public ResponseEntity<FlightSeatDTO> getFlightSeatDTOById(Long id) {
        log.info("get: FlightSeat by id={}", id);
        return (flightSeatService.getFlightSeatById(id).isEmpty()) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(FlightSeatMapper.INSTANCE.convertToFlightSeatDTOEntity(flightSeatService.getFlightSeatById(id).get(), flightService));
    }

    @Override
    public ResponseEntity<List<FlightSeatDTO>> getCheapestByFlightIdAndSeatCategory(Long flightID, CategoryType category) {
        log.info("getCheapestByFlightIdAndSeatCategory: get FlightSeats by flight ID = {} and seat category = {}", flightID, category);
        var flightSeats = flightSeatService.getCheapestFlightSeatsByFlightIdAndSeatCategory(flightID, category);
        if (flightSeats.isEmpty()) {
            log.error("getCheapestByFlightIdAndSeatCategory: FlightSeats with flightID = {} or seat category = {} not found", flightID, category);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(flightSeats, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Set<FlightSeatDTO>> generateAllFlightSeatsDTOByFlightId(Long flightId) {
        log.info("generate: FlightSeats by flightId. flightId={}", flightId);
        var flightSeats = flightSeatService.getFlightSeatsByFlightId(flightId);
        if (!flightSeats.isEmpty()) {
            return new ResponseEntity<>(flightSeats, HttpStatus.OK);
        }
        return new ResponseEntity<>(flightSeatService.addFlightSeatsByFlightId(flightId)
                .stream()
                .map(f -> FlightSeatMapper.INSTANCE.convertToFlightSeatDTOEntity(f, flightService))
                .collect(Collectors.toSet()),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<FlightSeatDTO> updateFlightSeatDTOById(Long id, FlightSeatDTO flightSeatDTO) {
        log.info("update: FlightSeat by id={}", id);
        if (flightSeatService.getFlightSeatById(id).isEmpty()) {
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
    public ResponseEntity<FlightSeatDTO> createFlightSeatDTO(FlightSeatDTO flightSeatDTO) {
        if (flightService.getFlightById(flightSeatDTO.getFlightId()) == null) {
            log.error("Flight with id = {} not found", flightSeatDTO.getFlightId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("create: Flight Seat saved with id= {}", flightSeatDTO.getId());
        return ResponseEntity.ok(flightSeatService.saveFlightSeatDTO(flightSeatDTO));
    }

    @Override
    public ResponseEntity<List<SeatDTO>> getAllSeatDTO() {
        var seats = flightSeatService.getAllSeatDTO();
        if (!seats.isEmpty()) {
            log.info("getAll: found {} Seats", seats.size());
            return ResponseEntity.ok(seats);
        } else {
            log.info("getAll: Seats not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
