package app.services.interfaces;

import app.dto.AircraftDTO;
import app.entities.Aircraft;
import org.springframework.data.domain.Page;

public interface AircraftService {

    Aircraft saveAircraft(AircraftDTO aircraftDTO);

    Page<Aircraft> getAllAircrafts(Integer page, Integer size);

    Aircraft getAircraftById(Long id);

    Aircraft getAircraftByAircraftNumber(String aircraftNumber);

    void deleteAircraftById(Long id);
}
