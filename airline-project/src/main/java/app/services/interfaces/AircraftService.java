package app.services.interfaces;

import app.dto.AircraftDto;
import app.entities.Aircraft;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AircraftService {
    List<AircraftDto> findAll();

    AircraftDto saveAircraft(AircraftDto aircraftDTO);

    Page<AircraftDto> getPage(Integer page, Integer size);

    Aircraft getAircraftById(Long id);

    Aircraft getAircraftByAircraftNumber(String aircraftNumber);

    void deleteAircraftById(Long id);
}
