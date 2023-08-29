package app.services;

import app.dto.AircraftDTO;
import app.entities.Aircraft;
import app.entities.Flight;
import app.mappers.AircraftMapper;
import app.repositories.AircraftRepository;
import app.repositories.FlightRepository;
import app.services.interfaces.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {

    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;
    private final AircraftMapper aircraftMapper;

    @Transactional
    public Aircraft saveAircraft(AircraftDTO aircraftDTO) {
        var aircraft = aircraftMapper.convertToAircraftEntity(aircraftDTO);
        if (!aircraft.getSeatSet().isEmpty()) {
            aircraft.getSeatSet().forEach(seat -> seat.setAircraft(aircraft));
        }
        return aircraftRepository.save(aircraft);
    }

    public Page<AircraftDTO> getAllAircrafts(Integer page, Integer size) {
        return aircraftRepository.findAll(PageRequest.of(page, size)).map(entity -> {
            return aircraftMapper.convertToAircarftDTOEntity(entity);
        });
    }

    public Aircraft getAircraftById(Long id) {
        return aircraftRepository.findById(id).orElse(null);
    }

    public Aircraft getAircraftByAircraftNumber(String aircraftNumber) {
        return aircraftRepository.findByAircraftNumber(aircraftNumber);
    }

    @Transactional
    public void deleteAircraftById(Long id) {
        List<Flight> flightSet = flightRepository.findByAircraft_Id(id);
        if (flightSet != null) {
            flightSet.forEach(flight -> flight.setAircraft(null));
        }
        aircraftRepository.deleteById(id);
    }

}