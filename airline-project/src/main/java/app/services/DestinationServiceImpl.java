package app.services;

import app.dto.DestinationDTO;
import app.entities.Destination;
import app.enums.Airport;
import app.repositories.DestinationRepository;
import app.services.interfaces.DestinationService;
import app.util.mappers.DestinationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;


@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {
    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;

    @Override
    public Page<DestinationDTO> getAllDestinations(Integer page, Integer size) {
        return destinationRepository.findAll(PageRequest.of(page, size)).map(destinationMapper::convertToDestinationDTOEntity);
    }

    @Override
    public Page<DestinationDTO> getDestinationByNameAndTimezone(Integer page, Integer size, String cityName, String countryName, String timezone) {
        if (cityName != null) {
            return destinationRepository.findByCityNameContainingIgnoreCase(PageRequest.of(page, size), cityName)
                    .map(destinationMapper::convertToDestinationDTOEntity);
        } else if(countryName != null) {
            return destinationRepository.findByCountryNameContainingIgnoreCase(PageRequest.of(page, size), countryName)
                    .map(destinationMapper::convertToDestinationDTOEntity);
        } else {
            return destinationRepository.findByTimezoneContainingIgnoreCase(PageRequest.of(page, size), timezone)
                    .map(destinationMapper::convertToDestinationDTOEntity);
        }
    }

    @Override
    @Transactional
    public void saveDestination(DestinationDTO destinationDTO) {
        var destination = destinationMapper.convertToDestinationEntity(destinationDTO);
        destinationRepository.save(destination);
    }

    @Override
    @Transactional
    public void updateDestinationById(Long id, DestinationDTO destinationDTO) {
        var destination = destinationMapper.convertToDestinationEntity(destinationDTO);
        destination.setId(id);
        destinationRepository.save(destination);
    }

    @Override
    public Destination getDestinationById(Long id) {
        return destinationRepository.findById(id).orElse(null);
    }

    @Override
    public Destination getDestinationByAirportCode(Airport airportCode) {
        return destinationRepository.findDestinationByAirportCode(airportCode).orElse(null);
    }

    @Override
    public void deleteDestinationById(Long id) {
        destinationRepository.deleteById(id);
    }
}
