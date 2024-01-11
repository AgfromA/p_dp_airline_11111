package app.controllers.rest;

import app.controllers.api.rest.TimezoneRestApi;
import app.dto.TimezoneDTO;
import app.entities.Timezone;
import app.mappers.TimezoneMapper;
import app.services.interfaces.TimezoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TimezoneRestController implements TimezoneRestApi {

    private final TimezoneService timezoneService;
    private final TimezoneMapper timezoneMapper = Mappers.getMapper(TimezoneMapper.class);

    @Override
    public ResponseEntity<List<TimezoneDTO>> getAllPagesTimezonesDTO(Integer page, Integer size) {
        log.info("getAll: get all Timezones");
        if (page == null || size == null) {
            log.info("getAll: get all List Timezones");
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            log.info("no correct data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var timezone = timezoneService.getAllPagesTimezones(page, size);

        return timezone.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(timezone.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<TimezoneDTO>> createUnPagedResponse() {
        var timezone = timezoneService.getAllTimeZone();
        if (timezone.isEmpty()) {
            log.error("getAll: Timezones not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll: found {} Timezones", timezone.size());
            return new ResponseEntity<>(timezone, HttpStatus.OK);
        }
    }


    @Override
    public ResponseEntity<TimezoneDTO> getTimezoneDTOById(Long id) {
        log.info("getById: search Timezone by id = {}", id);
        var timezone = timezoneService.getTimezoneById(id);

        if (timezone.isEmpty()) {
            log.info("getById: not found Timezone with id = {} doesn't exist", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new TimezoneDTO(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TimezoneDTO> createTimezoneDTO(TimezoneDTO timezoneDTO) {
        timezoneService.saveTimezone(timezoneDTO);
        log.info("create: new Timezone");
        return new ResponseEntity<>(new TimezoneDTO(),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TimezoneDTO> updateTimezoneDTOById(Long id, TimezoneDTO timezoneDTO) {
        timezoneDTO.setId(id);
        log.info("update: timezone = {}", timezoneDTO);

        Timezone updatedTimezone = timezoneService.updateTimezone(timezoneDTO);

        TimezoneDTO updatedTimezoneDTO = timezoneMapper.convertToTimezoneDTO(updatedTimezone);

        return new ResponseEntity<>(updatedTimezoneDTO, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<HttpStatus> deleteTimezoneById(Long id) {
        log.info("deleteAircraftById: deleting a Timezone with id = {}", id);
        try {
            timezoneService.deleteTimezoneById(id);
        } catch (Exception e) {
            log.error("deleteAircraftById: Timezone with id = {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}