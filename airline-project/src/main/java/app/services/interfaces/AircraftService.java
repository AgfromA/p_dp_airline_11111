package app.services.interfaces;

import app.dto.AircraftDTO;
import app.entities.Aircraft;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AircraftService {
    List<AircraftDTO> findAll();

    AircraftDTO saveAircraft(AircraftDTO aircraftDTO);

    Page<AircraftDTO> getPage(Integer page, Integer size);

    Aircraft getAircraftById(Long id);

    Aircraft getAircraftByAircraftNumber(String aircraftNumber);

    void deleteAircraftById(Long id);
}
