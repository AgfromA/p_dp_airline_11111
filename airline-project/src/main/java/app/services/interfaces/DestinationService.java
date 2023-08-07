package app.services.interfaces;

import app.entities.Destination;
import app.enums.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface DestinationService {

    Page<Destination> getAllDestinations(Pageable pageable);

    Page<Destination> getDestinationByNameAndTimezone(Pageable pageable, String cityName, String countryName, String timezone);

    void saveDestination(Destination destination);

    void updateDestinationById(Long id, Destination destination);

    Destination getDestinationById(Long id);

    Destination getDestinationByAirportCode(Airport airportCode);

    void deleteDestinationById(Long id);
}
