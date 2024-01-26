package app.controllers.rest;

import app.controllers.api.rest.TimezoneRestApi;
import app.dto.TimezoneDto;
import app.entities.Timezone;
import app.mappers.TimezoneMapper;
import app.services.TimezoneService;
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
    public ResponseEntity<List<TimezoneDto>> getAllTimezones(Integer page, Integer size) {
        log.info("getAll: get all Timezones");
        if (page == null || size == null) {
            log.info("getAll: get all List Timezones");
            return createUnPagedResponse();
        }

        var timezones = timezoneService.getAllPagesTimezones(page, size);
        return timezones.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(timezones.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<TimezoneDto>> createUnPagedResponse() {
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
    public ResponseEntity<TimezoneDto> getTimezoneById(Long id) {
        log.info("getById: search Timezone by id = {}", id);
        var timezone = timezoneService.getTimezoneById(id);

        if (timezone.isEmpty()) {
            log.info("getById: not found Timezone with id = {} doesn't exist", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new TimezoneDto(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TimezoneDto> createTimezone(TimezoneDto timezoneDto) {
        timezoneService.saveTimezone(timezoneDto);
        log.info("create: new Timezone");
        return new ResponseEntity<>(new TimezoneDto(),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TimezoneDto> updateTimezoneById(Long id, TimezoneDto timezoneDto) {
        timezoneDto.setId(id);
        log.info("update: timezone = {}", timezoneDto);

        Timezone updatedTimezone = timezoneService.updateTimezone(timezoneDto);

        TimezoneDto updatedTimezoneDto = timezoneMapper.toDto(updatedTimezone);

        return new ResponseEntity<>(updatedTimezoneDto, HttpStatus.OK);
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