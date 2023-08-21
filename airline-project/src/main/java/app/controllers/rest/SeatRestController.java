package app.controllers.rest;

import app.controllers.api.rest.SeatRestApi;
import app.dto.SeatDTO;

import app.exceptions.ViolationOfForeignKeyConstraintException;
import app.services.interfaces.AircraftService;
import app.services.interfaces.SeatService;
import app.util.mappers.SeatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SeatRestController implements SeatRestApi {

    private final SeatService seatService;
    private final AircraftService aircraftService;
    private final SeatMapper seatMapper;

    @Override
    public ResponseEntity<Page<SeatDTO>> getAllPagesSeatsDTO(Integer page, Integer size) {
        var seats = seatService.getAllPagesSeats(page, size).map(entity -> {
            return seatMapper.convertToSeatDTOEntity(entity);
        });
        if (!seats.isEmpty()) {
            log.info("getAll: found {} Seats", seats.getSize());
            return new ResponseEntity<>(seats, HttpStatus.OK);
        } else {
            log.info("getAll: Seats not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Page<SeatDTO>> getAllPagesSeatsDTOByAircraftId(Pageable pageable, Long aircraftId) {
        var seats = seatService.getPagesSeatsByAircraftId(aircraftId, pageable).map(entity -> {
            return seatMapper.convertToSeatDTOEntity(entity);

        });
        if (!seats.isEmpty()) {
            log.info("getAllByAircraftId: found {} Seats with aircraftId = {}", seats.getSize(), aircraftId);
            return new ResponseEntity<>(seats, HttpStatus.OK);
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
            return new ResponseEntity<>(new SeatDTO(seat), HttpStatus.OK);
        } else {
            log.info("getById: Seat not found. id = {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<SeatDTO> createSeatDTO(SeatDTO seatDTO) {
        var seat = seatMapper.convertToSeatEntity(seatDTO);
        seatService.saveSeat(seat);
        log.info("create: Seat saved with id= {}", seat.getId());
        return new ResponseEntity<>(new SeatDTO(seatService.getSeatById(seat.getId())), HttpStatus.CREATED);
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
        seatService.editSeatById(id, seatMapper.convertToSeatEntity(seatDTO));
        log.info("update: Seat with id = {} has been edited.", id);
        return new ResponseEntity<>(new SeatDTO(seatService.getSeatById(id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteSeatById(Long id) {
        try {
            seatService.deleteSeatById(id);
            log.info("deleteAircraftById: Seat with id={} has been deleted.", id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (ViolationOfForeignKeyConstraintException e) {
            log.error("deleteAircraftById: error of deleting - Seat with id={} is locked by FlightSeat.", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        } catch (EmptyResultDataAccessException e) {
            log.error("deleteAircraftById: error of deleting - Seat with id={} not found.", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}