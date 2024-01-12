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

    @Override
    public List<AircraftDTO> findAll() {
        return aircraftMapper.convertToAircarftDTOList(aircraftRepository.findAll());
    }

    @Transactional
    @Override
    public AircraftDTO saveAircraft(AircraftDTO aircraftDTO) {
        var aircraft = aircraftMapper.convertToAircraftEntity(aircraftDTO);
        if (!aircraft.getSeatSet().isEmpty()) {
            aircraft.getSeatSet().forEach(seat -> seat.setAircraft(aircraft));
        }
        return aircraftMapper.convertToAircarftDTOEntity(aircraftRepository.save(aircraft));
    }

    @Override
    public Page<AircraftDTO> getPage(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return aircraftRepository.findAll(pageRequest).map(aircraftMapper::convertToAircarftDTOEntity);
    }

    @Override
    public Aircraft getAircraftById(Long id) {
        return aircraftRepository.findById(id).orElse(null);
    }

    @Override
    public Aircraft getAircraftByAircraftNumber(String aircraftNumber) {
        return aircraftRepository.findByAircraftNumber(aircraftNumber);
    }

    @Transactional
    @Override
    public void deleteAircraftById(Long id) {
        List<Flight> flightSet = flightRepository.findByAircraft_Id(id);
        if (flightSet != null) {
            flightSet.forEach(flight -> flight.setAircraft(null));
        }
        aircraftRepository.deleteById(id);
    }
}