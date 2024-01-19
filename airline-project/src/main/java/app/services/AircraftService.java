package app.services;

import app.dto.AircraftDto;
import app.entities.Aircraft;
import app.entities.Flight;
import app.exceptions.EntityNotFoundException;
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
public class AircraftService {

    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;
    private final AircraftMapper aircraftMapper;

    public List<AircraftDto> findAll() {
        return aircraftMapper.toDtoList(aircraftRepository.findAll());
    }

    @Transactional
    public AircraftDto saveAircraft(AircraftDto aircraftDTO) {
        aircraftDTO.setId(null);
        var aircraft = aircraftMapper.toEntity(aircraftDTO);
        if (!aircraft.getSeatSet().isEmpty()) {
            aircraft.getSeatSet().forEach(seat -> seat.setAircraft(aircraft));
        }
        return aircraftMapper.toDto(aircraftRepository.save(aircraft));
    }

    @Transactional
    public AircraftDto updateAircraft(Long id, AircraftDto aircraftDTO) {
        var existAircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Operation was not finished because Aircraft was not found with id = " + id)
                );

        if (aircraftDTO.getAircraftNumber() == null) {
            aircraftDTO.setAircraftNumber(existAircraft.getAircraftNumber());
        }
        if (aircraftDTO.getModel() == null) {
            aircraftDTO.setModel(existAircraft.getModel());
        }
        if (aircraftDTO.getModelYear() == null) {
            aircraftDTO.setModelYear(existAircraft.getModelYear());
        }
        if (aircraftDTO.getFlightRange() == null) {
            aircraftDTO.setFlightRange(existAircraft.getFlightRange());
        }

        aircraftDTO.setId(id);

        var aircraft = aircraftMapper.toEntity(aircraftDTO);
        if (!aircraft.getSeatSet().isEmpty()) {
            aircraft.getSeatSet().forEach(seat -> seat.setAircraft(aircraft));
        }
        return aircraftMapper.toDto(aircraftRepository.save(aircraft));
    }

    public Page<AircraftDto> getPage(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return aircraftRepository.findAll(pageRequest).map(aircraftMapper::toDto);
    }

    public Aircraft getAircraftById(Long id) {
        return aircraftRepository.findById(id).orElse(null);
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