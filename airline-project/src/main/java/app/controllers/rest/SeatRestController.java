package app.controllers.rest;

import app.controllers.api.rest.SeatRestApi;
import app.dto.SeatDTO;

import app.exceptions.ViolationOfForeignKeyConstraintException;
import app.mappers.SeatMapper;
import app.services.interfaces.AircraftService;
import app.services.interfaces.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SeatRestController implements SeatRestApi {

    private final SeatService seatService;
    private final AircraftService aircraftService;

    @Override
    public ResponseEntity<List<SeatDTO>> getAllPagesSeatsDTO(Integer page, Integer size) {
        log.info("getAll: get all Seats");
        if (page == null || size == null) {
            log.info("getAll: get all list Seats");
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            log.info("no correct data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var seats = seatService.getAllPagesSeats(page, size);

        return seats.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(seats.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<SeatDTO>> createUnPagedResponse() {
        var seats = seatService.getAllSeats();
        if (seats.isEmpty()) {
            log.info("getAll: Seats not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll: found {} Seats", seats.size());
            return new ResponseEntity<>(seats, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<SeatDTO>> getAllPagesSeatsDTOByAircraftId(Pageable pageable, Long aircraftId) {
        var seats = seatService.getPagesSeatsByAircraftId(aircraftId, pageable);
        if (!seats.isEmpty()) {
            log.info("getAllByAircraftId: found {} Seats with aircraftId = {}", seats.getSize(), aircraftId);
            return new ResponseEntity<>(seats.getContent(), HttpStatus.OK);
        } else {
            log.info("getAllByAircraftId: Seats not found with aircraftId = {}", aircraftId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<SeatDTO> getSeatDTOById(Long id) {
        var seat = seatService.getSeatById(id);
        if (seat != null) {
            log.info("getById: Seat with id = {}", id);
            return new ResponseEntity<>(SeatMapper.INSTANCE.convertToSeatDTOEntity(seat), HttpStatus.OK);
        } else {
            log.info("getById: Seat not found. id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<SeatDTO> createSeatDTO(SeatDTO seatDTO) {
        if (aircraftService.getAircraftById(seatDTO.getAircraftId()) == null) {
            log.error("Aircraft with id = {} not found", seatDTO.getAircraftId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("create: Seat saved with id= {}", seatDTO.getId());
        return ResponseEntity.ok(SeatMapper.INSTANCE.convertToSeatDTOEntity(seatService.saveSeat(seatDTO)));
    }

    @Override
    public ResponseEntity<List<SeatDTO>> generateSeatsDTOByAircraftId(Long aircraftId) {
        if (aircraftService.getAircraftById(aircraftId) == null) {
            log.error("generate: Aircraft with id = {} not found", aircraftId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var savedSeats = seatService.generateSeatsDTOByAircraftId(aircraftId);

        if (savedSeats.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.info("generate: saved {} new Seats with aircraft.id = {}", savedSeats.size(), aircraftId);
            return new ResponseEntity<>(savedSeats, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<SeatDTO> updateSeatDTOById(Long id, SeatDTO seatDTO) {
        if (seatService.getSeatById(id) == null) {
            log.error("Seat not found id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (aircraftService.getAircraftById(seatDTO.getAircraftId()) == null) {
            log.error("Aircraft with id = {} not found", seatDTO.getAircraftId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        seatService.editSeatById(id, seatDTO);
        log.info("update: Seat with id = {} has been edited.", id);

        return new ResponseEntity<>(SeatMapper.INSTANCE.convertToSeatDTOEntity(seatService.getSeatById(id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteSeatById(Long id) {
        try {
            seatService.deleteSeatById(id);
            log.info("deleteSeatById: Seat with id={} has been deleted.", id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (ViolationOfForeignKeyConstraintException e) {
            log.error("deleteSeatById: error of deleting - Seat with id={} is locked by FlightSeat.", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        } catch (EmptyResultDataAccessException e) {
            log.error("deleteSeatById: error of deleting - Seat with id={} not found.", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}