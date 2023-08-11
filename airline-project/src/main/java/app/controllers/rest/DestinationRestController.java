package app.controllers.rest;

import app.controllers.api.rest.DestinationRestApi;
import app.dto.DestinationDTO;
import app.services.interfaces.DestinationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class DestinationRestController implements DestinationRestApi {

    private final DestinationService destinationService;

    @Override
    public ResponseEntity<Page<DestinationDTO>> getAllPagesDestinationsDTO(Pageable pageable, String cityName, String countryName, String timezone) {
        Page<DestinationDTO> destination = null;
        if (cityName == null && countryName == null && timezone == null) {
            destination = destinationService.getAllDestinations(pageable);
            log.info("getAll: get all Destinations");
        } else {
            log.info("getAll: get Destinations by cityName or countryName or timezone. countryName = {}. cityName= {}. timezone = {}", countryName, cityName, timezone);
            destination = destinationService.getDestinationByNameAndTimezone(pageable, cityName, countryName, timezone);
        }
        return (!destination.isEmpty())
                ? new ResponseEntity<>(destination, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<DestinationDTO> createDestinationDTO(DestinationDTO destinationDTO) {
        log.info("create: create new Destination");
        destinationService.saveDestination(destinationDTO);
        return new ResponseEntity<>(destinationDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DestinationDTO> updateDestinationDTOById(Long id, DestinationDTO destinationDTO) {
        log.info("update: update Destination with id={}", id);
        destinationService.updateDestinationById(id, destinationDTO);
        var updatedDestination = destinationService.getDestinationById(id);
        if (updatedDestination != null) {
            return new ResponseEntity<>(updatedDestination, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteDestinationById(Long id) {
        log.info("deleteAircraftById: deleteAircraftById Destination with id={}", id);
        destinationService.deleteDestinationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}