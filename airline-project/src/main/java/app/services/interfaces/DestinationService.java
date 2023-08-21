package app.services.interfaces;

import app.dto.DestinationDTO;
import app.entities.Destination;
import app.enums.Airport;
import org.springframework.data.domain.Page;



public interface DestinationService {

    Page<DestinationDTO> getAllDestinations(Integer page, Integer size);

    Page<DestinationDTO> getDestinationByNameAndTimezone(Integer page, Integer size, String cityName, String countryName, String timezone);

    void saveDestination(DestinationDTO destinationDTO);

    void updateDestinationById(Long id, DestinationDTO destinationDTO);

    DestinationDTO getDestinationById(Long id);

    Destination getDestinationByAirportCode(Airport airportCode);

    void deleteDestinationById(Long id);
}
