package app.services;

import app.dto.DestinationDto;
import app.entities.Destination;
import app.enums.Airport;
import app.mappers.DestinationMapper;
import app.repositories.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;

    public List<DestinationDto> getAllDestinationDTO() {
        return destinationMapper.convertToDestinationDtoList(destinationRepository.findAll());
    }

    public Page<DestinationDto> getAllDestinations(Integer page, Integer size) {
        return destinationRepository.findAll(PageRequest.of(page, size)).map(destinationMapper::convertToDestinationDtoEntity);
    }

    public Page<DestinationDto> getDestinationByNameAndTimezone(Integer page, Integer size, String cityName, String countryName, String timezone) {
        if (cityName != null && !cityName.isEmpty()) {
            return destinationRepository.findByCityNameContainingIgnoreCase(PageRequest.of(page, size), cityName)
                    .map(destinationMapper::convertToDestinationDtoEntity);
        } else if (countryName != null && !countryName.isEmpty()) {
            return destinationRepository.findByCountryNameContainingIgnoreCase(PageRequest.of(page, size), countryName)
                    .map(destinationMapper::convertToDestinationDtoEntity);
        } else {
            return destinationRepository.findByTimezoneContainingIgnoreCase(PageRequest.of(page, size), timezone)
                    .map(destinationMapper::convertToDestinationDtoEntity);
        }
    }

    @Transactional
    public DestinationDto saveDestination(DestinationDto destinationDTO) {
        var destination = destinationMapper.convertToDestinationEntity(destinationDTO);
        return destinationMapper.convertToDestinationDtoEntity(destinationRepository.save(destination));
    }

    @Transactional
    public void updateDestinationById(Long id, DestinationDto destinationDTO) {
        var destination = destinationMapper.convertToDestinationEntity(destinationDTO);
        destination.setId(id);
        destinationRepository.save(destination);
    }

    public DestinationDto getDestinationById(Long id) {
        return destinationMapper.convertToDestinationDtoEntity(destinationRepository.findById(id).orElse(null));
    }

    public Destination getDestinationByAirportCode(Airport airportCode) {
        return destinationRepository.findDestinationByAirportCode(airportCode).orElse(null);
    }

    public void deleteDestinationById(Long id) {
        destinationRepository.deleteById(id);
    }
}