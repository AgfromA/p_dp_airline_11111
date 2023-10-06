package app.controllers.rest;

import app.controllers.api.rest.TimezoneRestApi;
import app.dto.TimezoneDTO;
import app.entities.Timezone;
import app.mappers.TimezoneMapper;
import app.services.interfaces.TimezoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TimezoneRestController implements TimezoneRestApi {

    private final TimezoneService timezoneService;
    private final TimezoneMapper timezoneMapper = Mappers.getMapper(TimezoneMapper.class);

    @Override
    public ResponseEntity<Page<TimezoneDTO>> getAllPagesTimezonesDTO(Integer page, Integer size) {
        var timezone = timezoneService.getAllPagesTimezones(page, size);
        if (timezone == null) {
            log.error("getAll: Timezones not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("getAll: Find all timezones");

        var timezoneDTOS = timezone.stream().map(tz -> {
            TimezoneDTO dto = new TimezoneDTO();
            dto.setId(tz.getId());
            dto.setCountryName(tz.getCountryName());
            dto.setCityName(tz.getCityName());
            dto.setGmt(tz.getGmt());
            dto.setGmtWinter(tz.getGmtWinter());
            return dto;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(new PageImpl<>(timezoneDTOS, PageRequest.of(page, size), timezone.getTotalElements()), HttpStatus.OK);
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