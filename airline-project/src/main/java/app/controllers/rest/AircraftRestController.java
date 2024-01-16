package app.controllers.rest;

import app.controllers.api.rest.AircraftRestApi;
import app.dto.AircraftDto;
import app.mappers.AircraftMapper;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.dao.EmptyResultDataAccessException;
import app.services.AircraftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AircraftRestController implements AircraftRestApi {

    private final AircraftService aircraftService;
    private final AircraftMapper aircraftMapper;

    @Override
    public ResponseEntity<List<AircraftDto>> getAllAircrafts(Integer page, Integer size) {
        log.info("getAll: get all Aircrafts");
        if (page == null || size == null) {
            log.info("getAll: get all List Aircrafts");
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            log.info("getAll: no correct data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var aircraft = aircraftService.getPage(page, size);
        return aircraft.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(aircraft.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<AircraftDto>> createUnPagedResponse() {
        var aircraft = aircraftService.findAll();
        if (aircraft.isEmpty()) {
            log.info("getAll: Aircrafts not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll: found {} Aircrafts", aircraft.size());
            return new ResponseEntity<>(aircraft, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<AircraftDto> getAircraftById(Long id) {
        var aircraft = aircraftService.getAircraftById(id);
        if (aircraft == null) {
            log.error("getById: Aircraft with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("getById: Aircraft with id={} returned.", id);
        return new ResponseEntity<>(aircraftMapper.toDto(aircraft), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AircraftDto> createAircraft(AircraftDto aircraftDTO) {
        log.info("create: new Aircraft saved.");
        return new ResponseEntity<>(aircraftService.saveAircraft(aircraftDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AircraftDto> updateAircraftById(Long id, AircraftDto aircraftDTO) {
        if (aircraftService.getAircraftById(id) == null) {
            log.error("update: Aircraft with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("update: the Aircraft with id={} has been edited.", id);
        return ResponseEntity.ok(aircraftService.updateAircraft(id, aircraftDTO));
    }

    /*
     * Не удаляет aircraft, если у него есть seat
     */
    @Override
    public ResponseEntity<HttpStatus> deleteAircraftById(Long id) {
        try {
            aircraftService.deleteAircraftById(id);
            log.info("deleteAircraftById: the Aircraft with id={} has been deleted.", id);
            return ResponseEntity.ok().build();
        } catch (TransactionSystemException e) {
            log.error("deleteAircraftById: error of deleting - Aircraft with id={} has seats referring to it", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        } catch (EmptyResultDataAccessException e) {
            log.error("deleteAircraftById: error of deleting - Aircraft with id={} not found. {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}