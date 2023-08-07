package app.services.interfaces;

import app.entities.Aircraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AircraftService {

    Aircraft saveAircraft(Aircraft aircraft);

    Page<Aircraft> getAllAircrafts(Pageable pageable);

    Aircraft getAircraftById(Long id);

    Aircraft getAircraftByAircraftNumber(String aircraftNumber);

    void deleteAircraftById(Long id);
}
