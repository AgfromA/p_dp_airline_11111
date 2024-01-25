package app.services;

import app.dto.AircraftDto;
import app.entities.Aircraft;
import app.exceptions.EntityNotFoundException;
import app.mappers.AircraftMapper;
import app.repositories.AircraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AircraftService {

    private final AircraftRepository aircraftRepository;
    private final AircraftMapper aircraftMapper;

    public List<AircraftDto> getAllAircrafts() {
        return aircraftMapper.toDtoList(aircraftRepository.findAll());
    }

    public Page<AircraftDto> getAllAircrafts(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return aircraftRepository.findAll(pageRequest).map(aircraftMapper::toDto);
    }

    public Aircraft getAircraft(Long id) {
        return aircraftRepository.findById(id).orElse(null);
    }

    public Optional<AircraftDto> getAircraftDto(Long id) {
        return aircraftRepository.findById(id).map(aircraftMapper::toDto);
    }

    @Transactional
    public AircraftDto createAircraft(AircraftDto aircraftDTO) {
        aircraftDTO.setId(null);
        var aircraft = aircraftMapper.toEntity(aircraftDTO);
        return aircraftMapper.toDto(aircraftRepository.save(aircraft));
    }

    @Transactional
    public AircraftDto updateAircraft(Long id, AircraftDto aircraftDTO) {
        var existingAircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Operation was not finished because Aircraft was not found with id = " + id)
                );

        if (aircraftDTO.getAircraftNumber() != null) {
            existingAircraft.setAircraftNumber(aircraftDTO.getAircraftNumber());
        }
        if (aircraftDTO.getModel() != null) {
            existingAircraft.setModel(aircraftDTO.getModel());
        }
        if (aircraftDTO.getModelYear() != null) {
            existingAircraft.setModelYear(aircraftDTO.getModelYear());
        }
        if (aircraftDTO.getFlightRange() != null) {
            existingAircraft.setFlightRange(aircraftDTO.getFlightRange());
        }
        return aircraftMapper.toDto(aircraftRepository.save(existingAircraft));
    }

    @Transactional
    public void deleteAircraft(Long id) {
        aircraftRepository.deleteById(id);
    }
}