package app.controllers.rest;

import app.controllers.api.rest.AircraftRestApi;
import app.dto.AircraftDto;
import app.services.AircraftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class AircraftRestController implements AircraftRestApi {

    private final AircraftService aircraftService;

    @Override
    public ResponseEntity<Page<AircraftDto>> getAllAircrafts(Integer page, Integer size) {
        log.info("getAllAircrafts:");
        if (page == null || size == null) {
            return createUnPagedResponse();
        }

        var aircrafts = aircraftService.getAllAircrafts(page, size);
        return aircrafts.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(aircrafts, HttpStatus.OK);
    }

    private ResponseEntity<Page<AircraftDto>> createUnPagedResponse() {
        var aircraft = aircraftService.getAllAircrafts();
        if (aircraft.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAllAircrafts: count {}", aircraft.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(aircraft)));
        }
    }

    @Override
    public ResponseEntity<AircraftDto> getAircraft(Long id) {
        log.info("getAircraft: by id={}", id);
        var aircraft = aircraftService.getAircraftDto(id);
        return aircraft.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<AircraftDto> createAircraft(AircraftDto aircraftDTO) {
        log.info("createAircraft:");
        return new ResponseEntity<>(aircraftService.createAircraft(aircraftDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AircraftDto> updateAircraft(Long id, AircraftDto aircraftDTO) {
        log.info("updateAircraft: by id={}", id);
        return ResponseEntity.ok(aircraftService.updateAircraft(id, aircraftDTO));
    }

    @Override
    public ResponseEntity<HttpStatus> deleteAircraft(Long id) {
        aircraftService.deleteAircraft(id);
        log.info("deleteAircraft: id={}", id);
        return ResponseEntity.ok().build();
    }
}