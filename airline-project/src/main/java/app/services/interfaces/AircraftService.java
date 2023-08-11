package app.services.interfaces;

import app.dto.AircraftDTO;
import app.entities.Aircraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AircraftService {

    Aircraft saveAircraft(AircraftDTO aircraftDTO);

    Page<AircraftDTO> getAllAircrafts(Pageable pageable);

    Aircraft getAircraftById(Long id);

    Aircraft getAircraftByAircraftNumber(String aircraftNumber);

    void deleteAircraftById(Long id);
}
