package app.controllers.rest;

import app.controllers.api.rest.AircraftRestApi;
import app.dto.AircraftDTO;
import app.entities.Aircraft;
import app.mappers.AircraftMapper;
import app.services.interfaces.AircraftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AircraftRestController implements AircraftRestApi {

    private final AircraftService aircraftService;
    private final AircraftMapper aircraftMapper = Mappers.getMapper(AircraftMapper.class);

    @Override
    public ResponseEntity<Page<AircraftDTO>> getAllPagesAircraftsDTO(Integer page, Integer size) {
        log.info("getAll: get all Aircrafts");
        Page<AircraftDTO> aircrafts = aircraftService.getAllAircrafts(page, size);
        return aircrafts.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(aircrafts, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AircraftDTO> getAircraftDTOById(Long id) {
        var aircraft = aircraftService.getAircraftById(id);
        if (aircraft == null) {
            log.error("getById: Aircraft with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("getById: Aircraft with id={} returned.", id);
        return new ResponseEntity<>(aircraftMapper.convertToAircarftDTOEntity(aircraft), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AircraftDTO> createAircraft(AircraftDTO aircraftDTO) {
        log.info("create: new Aircraft saved.");
        return new ResponseEntity<>(aircraftMapper.convertToAircarftDTOEntity(aircraftService.saveAircraft(aircraftDTO)),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AircraftDTO> updateAircraftById(Long id, AircraftDTO aircraftDTO) {
        if (aircraftService.getAircraftById(id) == null) {
            log.error("update: Aircraft with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        aircraftDTO.setId(id);
        log.info("update: the Aircraft with id={} has been edited.", id);
        return ResponseEntity.ok(aircraftMapper.convertToAircarftDTOEntity(aircraftService.saveAircraft(aircraftDTO)));
    }

    @Override
    public ResponseEntity<HttpStatus> deleteAircraftById(Long id) {
        try {
            aircraftService.deleteAircraftById(id);
            log.info("deleteAircraftById: the Aircraft with id={} has been deleted.", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("deleteAircraftById: error of deleting - Aircraft with id={} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }
}