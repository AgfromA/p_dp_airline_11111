package app.controllers.rest;

import app.controllers.api.rest.DestinationRestApi;
import app.dto.DestinationDTO;
import app.entities.Destination;
import app.mappers.DestinationMapper;
import app.services.interfaces.DestinationService;
import app.util.LogsUtils;
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
public class DestinationRestController implements DestinationRestApi {

    private final DestinationService destinationService;

    @Override
    public ResponseEntity<Page<DestinationDTO>> getAllPagesDestinationsDTO(Integer page, Integer size, String cityName, String countryName, String timezone) {
        Page<DestinationDTO> destination = null;
        if (cityName == null && countryName == null && timezone == null) {
            destination = destinationService.getAllDestinations(page, size);
            log.info("get all Destinations: found {} Destination", destination.getNumberOfElements());
        } else {
            destination = destinationService.getDestinationByNameAndTimezone(page, size, cityName, countryName, timezone);
            log.info("get all Destinations by cityName or countryName or timezone. countryName = {}. cityName= {}. timezone = {}: found {} Destination", countryName, cityName, timezone, destination.getNumberOfElements());
        }
        return (!destination.isEmpty())
                ? new ResponseEntity<>(destination, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<DestinationDTO> createDestinationDTO(DestinationDTO destinationDTO) {
        Destination existingDestination = destinationService.getDestinationByAirportCode(destinationDTO.getAirportCode());
        log.info("create: new Destination - {}", LogsUtils.objectToJson(destinationDTO));
        return new ResponseEntity<>(Mappers.getMapper(DestinationMapper.class)
                .convertToDestinationDTOEntity(destinationService.saveDestination(destinationDTO)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DestinationDTO> updateDestinationDTOById(Long id, DestinationDTO destinationDTO) {
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