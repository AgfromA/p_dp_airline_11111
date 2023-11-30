package app.services.interfaces;

import app.dto.DestinationDTO;
import app.entities.Destination;
import app.enums.Airport;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface DestinationService {

    List<DestinationDTO> getAllDestinationDTO();

    Page<DestinationDTO> getAllDestinations(Integer page, Integer size);

    Page<DestinationDTO> getDestinationByNameAndTimezone(Integer page, Integer size, String cityName, String countryName, String timezone);

    Destination saveDestination(DestinationDTO destinationDTO);

    void updateDestinationById(Long id, DestinationDTO destinationDTO);

    DestinationDTO getDestinationById(Long id);

    Destination getDestinationByAirportCode(Airport airportCode);

    void deleteDestinationById(Long id);
}
