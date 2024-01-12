package app.controllers.rest;

import app.controllers.api.rest.DestinationRestApi;
import app.dto.DestinationDto;
import app.entities.Destination;
import app.services.DestinationService;
import app.utils.LogsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DestinationRestController implements DestinationRestApi {

    private final DestinationService destinationService;

    @Override
    public ResponseEntity<List<DestinationDto>> getAllDestinations(Integer page, Integer size, String cityName,
                                                                   String countryName, String timezone) {
        log.info("get all Destinations");
        if (page == null || size == null) {
            log.info("get all List Destinations");
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            log.info("no correct data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Page<DestinationDto> destination;
        if (cityName == null && countryName == null && timezone == null) {
            destination = destinationService.getAllDestinations(page, size);
            log.info("get all Destinations: found {} Destination", destination.getNumberOfElements());
        } else {
            destination = destinationService.getDestinationByNameAndTimezone(page, size, cityName, countryName, timezone);
            log.info("get all Destinations by cityName or countryName or timezone. countryName = {}. cityName= {}. timezone = {}: found {} Destination",
                    countryName, cityName, timezone, destination.getNumberOfElements());
        }
        return destination.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(destination.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<DestinationDto>> createUnPagedResponse() {
        var destinations = destinationService.getAllDestinationDTO();
        if (destinations.isEmpty()) {
            log.info("Destinations not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("found {} Destinations", destinations.size());
            return new ResponseEntity<>(destinations, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<DestinationDto> createDestination(DestinationDto destinationDTO) {
        Destination existingDestination = destinationService.getDestinationByAirportCode(destinationDTO.getAirportCode());
        log.info("create: new Destination - {}", LogsUtils.objectToJson(destinationDTO));
        return new ResponseEntity<>(destinationService.saveDestination(destinationDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DestinationDto> updateDestinationById(Long id, DestinationDto destinationDTO) {
        log.info("update: Destination with id={}", id);
        destinationService.updateDestinationById(id, destinationDTO);
        var updatedDestinationDTO = destinationService.getDestinationById(id);
        if (updatedDestinationDTO != null) {
            return new ResponseEntity<>(updatedDestinationDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteDestinationById(Long id) {
        log.info("delete: Destination with id={}", id);
        destinationService.deleteDestinationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}