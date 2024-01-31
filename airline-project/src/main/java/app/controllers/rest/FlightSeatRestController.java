package app.controllers.rest;

import app.controllers.api.rest.FlightSeatRestApi;
import app.dto.FlightSeatDto;
import app.services.FlightSeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public ResponseEntity<Page<FlightSeatDto>> getAllFlightSeats(Integer page,
                                                                 Integer size,
                                                                 Long flightId,
                                                                 Boolean isSold,
                                                                 Boolean isRegistered) {
        log.info("getAllFlightSeats:");
        if (page == null || size == null) {
            return createUnPagedResponse();
        }

        Page<FlightSeatDto> flightSeats;
        if (flightId == null && isSold == null && isRegistered == null) {
            flightSeats = flightSeatService.getAllFlightSeats(page, size);
        } else {
            log.info("getAllFlightSeats: filtered");
            flightSeats = flightSeatService.getAllFlightSeatsFiltered(page, size, flightId, isSold, isRegistered);
        }
        return flightSeats.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : ResponseEntity.ok(flightSeats);
    }

    private ResponseEntity<Page<FlightSeatDto>> createUnPagedResponse() {
        var flightSeats = flightSeatService.getAllFlightSeats();
        if (flightSeats.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAllFlightSeats: count: {}", flightSeats.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(flightSeats)));
        }
    }

    @Override
    public ResponseEntity<FlightSeatDto> getFlightSeat(Long id) {
        log.info("getFlightSeat: by id: {}", id);
        var flightSeat = flightSeatService.getFlightSeatDto(id);
        return flightSeat.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<FlightSeatDto> createFlightSeat(FlightSeatDto flightSeat) {
        log.info("createFlightSeat:");
        return new ResponseEntity<>(flightSeatService.createFlightSeat(flightSeat), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<FlightSeatDto> updateFlightSeat(Long id, FlightSeatDto flightSeatDTO) {
        log.info("updateFlightSeat: by id: {}", id);
        return ResponseEntity.ok(flightSeatService.editFlightSeat(id, flightSeatDTO));
    }

    @Override
    public ResponseEntity<String> deleteFlightSeat(Long id) {
        log.info("deleteFlightSeat: by id: {}", id);
        flightSeatService.deleteFlightSeatById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Page<FlightSeatDto>> generateFlightSeats(Long flightId) {
        log.info("generateFlightSeats: by flightId: {}", flightId);
        List<FlightSeatDto> flightSeatsList = flightSeatService.generateFlightSeats(flightId);
        Page<FlightSeatDto> flightSeatsPage = new PageImpl<>(flightSeatsList);
        return new ResponseEntity<>(flightSeatsPage, HttpStatus.CREATED);
    }
}