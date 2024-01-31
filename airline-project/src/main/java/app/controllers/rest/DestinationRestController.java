package app.controllers.rest;

import app.controllers.api.rest.DestinationRestApi;
import app.dto.DestinationDto;
import app.services.DestinationService;
import app.utils.LogsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DestinationRestController implements DestinationRestApi {

    private final DestinationService destinationService;

    @Override
    public ResponseEntity<Page<DestinationDto>> getAllDestinations(Integer page, Integer size, String cityName,
                                                                   String countryName, String timezone) {
        log.info("get all Destinations");
        if (page == null || size == null) {
            log.info("get all List Destinations");
            return createUnPagedResponse();
        }

        Page<DestinationDto> destinations;
        if (cityName == null && countryName == null && timezone == null) {
            destinations = destinationService.getAllDestinations(page, size);
            log.info("get all Destinations: found {} Destination", destinations.getNumberOfElements());
        } else {
            destinations = destinationService.getDestinationByNameAndTimezone(page, size, cityName, countryName, timezone);
            log.info("get all Destinations by cityName or countryName or timezone. countryName: {}. cityName: {}. timezone: {} found {} Destination",
                    countryName, cityName, timezone, destinations.getNumberOfElements());
        }
        return destinations.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(destinations, HttpStatus.OK);
    }

    private ResponseEntity<Page<DestinationDto>> createUnPagedResponse() {
        var destinations = destinationService.getAllDestinationDTO();
        if (destinations.isEmpty()) {
            log.info("Destinations not found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("found {} Destinations", destinations.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(destinations)));
        }
    }

    @Override
    public ResponseEntity<DestinationDto> createDestination(DestinationDto destinationDTO) {
        log.info("create: new Destination: {}", LogsUtils.objectToJson(destinationDTO));
        return new ResponseEntity<>(destinationService.saveDestination(destinationDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DestinationDto> updateDestinationById(Long id, DestinationDto destinationDTO) {
        log.info("update: Destination with id: {}", id);
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
        log.info("delete: Destination with id: {}", id);
        destinationService.deleteDestinationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}