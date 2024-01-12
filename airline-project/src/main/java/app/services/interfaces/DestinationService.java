package app.services.interfaces;

import app.dto.DestinationDto;
import app.entities.Destination;
import app.enums.Airport;
import org.springframework.data.domain.Page;

import java.util.List;


public interface DestinationService {

    List<DestinationDto> getAllDestinationDTO();

    Page<DestinationDto> getAllDestinations(Integer page, Integer size);

    Page<DestinationDto> getDestinationByNameAndTimezone(Integer page, Integer size, String cityName, String countryName, String timezone);

    DestinationDto saveDestination(DestinationDto destinationDTO);

    void updateDestinationById(Long id, DestinationDto destinationDTO);

    DestinationDto getDestinationById(Long id);

    Destination getDestinationByAirportCode(Airport airportCode);

    void deleteDestinationById(Long id);
}
