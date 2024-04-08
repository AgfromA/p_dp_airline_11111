package app.services;

import app.dto.DestinationDto;
import app.entities.Destination;
import app.enums.Airport;
import app.mappers.DestinationMapper;
import app.repositories.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;

    public List<DestinationDto> getAllDestinations() {
        return destinationMapper.toDtoList(destinationRepository.findAll());
    }

    public Page<DestinationDto> getAllDestinationsPaginated(Integer page, Integer size) {
        if (page == null || size == null) throw new IllegalArgumentException("Page and size must not be null");
        return destinationRepository.findAll(PageRequest.of(page, size)).map(destinationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<DestinationDto> getAllDestinationsFilteredPaginated(Integer page, Integer size, String cityName, String countryName, String timezone) {
        if (cityName != null && !cityName.isEmpty()) {
            return destinationRepository.findByCityNameContainingIgnoreCase(PageRequest.of(page, size), cityName)
                    .map(destinationMapper::toDto);
        } else if (countryName != null && !countryName.isEmpty()) {
            return destinationRepository.findByCountryNameContainingIgnoreCase(PageRequest.of(page, size), countryName)
                    .map(destinationMapper::toDto);
        } else  {
            return destinationRepository.findByTimezoneContainingIgnoreCase(PageRequest.of(page, size), timezone)
                    .map(destinationMapper::toDto);
        }
    }
    @Transactional(readOnly = true)
    public List<DestinationDto> getAllDestinationsFiltered(String cityName, String countryName, String timezone) {
        if (cityName != null && !cityName.isEmpty()) {
            return destinationRepository.findByCityName(cityName)
                    .stream()
                    .map(destinationMapper::toDto)
                    .collect(Collectors.toList());
        } else if (countryName != null && !countryName.isEmpty()) {
            return destinationRepository.findByCountryName(countryName)
                    .stream()
                    .map(destinationMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return destinationRepository.findByTimezone(timezone)
                    .stream()
                    .map(destinationMapper::toDto)
                    .collect(Collectors.toList());
        }
    }


    @Transactional
    public DestinationDto saveDestination(DestinationDto destinationDTO) {
        destinationDTO.setId(null);
        var destination = destinationMapper.toEntity(destinationDTO);
        return destinationMapper.toDto(destinationRepository.save(destination));
    }

    @Transactional
    public void updateDestinationById(Long id, DestinationDto destinationDTO) {
        var destination = destinationMapper.toEntity(destinationDTO);
        destination.setId(id);
        destinationRepository.save(destination);
    }

    public DestinationDto getDestinationById(Long id) {
        return destinationMapper.toDto(destinationRepository.findById(id).orElse(null));
    }

    public Destination getDestinationByAirportCode(Airport airportCode) {
        return destinationRepository.findDestinationByAirportCode(airportCode).orElse(null);
    }

    @Transactional
    public void deleteDestinationById(Long id) {
        destinationRepository.deleteById(id);
    }
}