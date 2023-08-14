package app.services.interfaces;

import app.entities.Aircraft;
import org.springframework.data.domain.Page;

public interface AircraftService {

    Aircraft saveAircraft(Aircraft aircraft);

    Page<Aircraft> getAllAircrafts(Integer page, Integer size);

    Aircraft getAircraftById(Long id);

    Aircraft getAircraftByAircraftNumber(String aircraftNumber);

    void deleteAircraftById(Long id);
}
