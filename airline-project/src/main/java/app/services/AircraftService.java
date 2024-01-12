package app.services;

import app.dto.AircraftDto;
import app.entities.Aircraft;
import app.entities.Flight;
import app.mappers.AircraftMapper;
import app.repositories.AircraftRepository;
import app.repositories.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AircraftService  {

    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;
    private final AircraftMapper aircraftMapper;

    public List<AircraftDto> findAll() {
        return aircraftMapper.convertToAircarftDTOList(aircraftRepository.findAll());
    }

    @Transactional
    public AircraftDto saveAircraft(AircraftDto aircraftDTO) {
        var aircraft = aircraftMapper.convertToAircraftEntity(aircraftDTO);
        if (!aircraft.getSeatSet().isEmpty()) {
            aircraft.getSeatSet().forEach(seat -> seat.setAircraft(aircraft));
        }
        return aircraftMapper.convertToAircarftDTOEntity(aircraftRepository.save(aircraft));
    }

    public Page<AircraftDto> getPage(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return aircraftRepository.findAll(pageRequest).map(aircraftMapper::convertToAircarftDTOEntity);
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