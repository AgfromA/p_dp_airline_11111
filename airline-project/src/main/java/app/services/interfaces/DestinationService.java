package app.services.interfaces;

import app.dto.DestinationDTO;
import app.entities.Destination;
import app.enums.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DestinationService {

    Page<DestinationDTO> getAllDestinations(Pageable pageable);

    Page<DestinationDTO> getDestinationByNameAndTimezone(Pageable pageable, String cityName, String countryName, String timezone);

    void saveDestination(DestinationDTO destinationDTO);

    void updateDestinationById(Long id, DestinationDTO destinationDTO);

    DestinationDTO getDestinationById(Long id);

    DestinationDTO getDestinationByAirportCode(Airport airportCode);

    void deleteDestinationById(Long id);
}
