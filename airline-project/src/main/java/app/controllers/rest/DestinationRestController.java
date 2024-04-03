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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class DestinationRestController implements DestinationRestApi {

    private final DestinationService destinationService;

    @Override
    public ResponseEntity<Page<DestinationDto>> getAllDestinations(Integer page, Integer size, String cityName, String countryName, String timezone) {



        if(page == null && size == null && cityName == null && countryName == null && timezone == null){
            return createUnPagedResponse();
        } else if (cityName == null && countryName == null && timezone == null) {
            Page<DestinationDto> destinations1 = destinationService.getAllDestinations(page, size);
            return ResponseEntity.ok(destinations1);
        } else if (page == null && size == null ) {
            List<DestinationDto> dtos = destinationService.getByNameOrTimeZoneOrCountry(cityName, countryName, timezone);
            return ResponseEntity.ok(new PageImpl<>(dtos));
        } else {
            Page<DestinationDto> destinations = destinationService.getDestinationByNameAndTimezone(page, size, cityName, countryName, timezone);
            return ResponseEntity.ok(destinations);
        }
    }


    public ResponseEntity<Page<DestinationDto>> createUnPagedResponse() {
        var destinations = destinationService.getAllDestinationDTO();
        if (destinations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAllDestinations: count: {}", destinations.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(destinations)));
        }
    }

    @Override
    public ResponseEntity<DestinationDto> createDestination(DestinationDto destinationDTO) {
        log.info("createDestination:");
        return new ResponseEntity<>(destinationService.saveDestination(destinationDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DestinationDto> updateDestination(Long id, DestinationDto destinationDTO) {
        log.info("updateDestination: by id: {}", id);
        destinationService.updateDestinationById(id, destinationDTO);
        var updatedDestinationDTO = destinationService.getDestinationById(id);
        if (updatedDestinationDTO != null) {
            return new ResponseEntity<>(updatedDestinationDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteDestination(Long id) {
        log.info("deleteDestinationById: by id: {}", id);
        destinationService.deleteDestinationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}