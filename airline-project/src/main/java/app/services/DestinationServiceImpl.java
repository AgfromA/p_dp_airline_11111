package app.services;

import app.dto.DestinationDTO;
import app.entities.Destination;
import app.enums.Airport;
import app.repositories.DestinationRepository;
import app.services.interfaces.DestinationService;
import app.util.mappers.DestinationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;

    @Override
    public Page<DestinationDTO> getAllDestinations(Pageable pageable) {
        return destinationRepository.findAll(pageable).map(entity -> {
            var dto = destinationMapper.convertToDestinationDTOEntity(entity);
            return dto;
        });
    }

    @Override
    public Page<DestinationDTO> getDestinationByNameAndTimezone(Pageable pageable, String cityName, String countryName, String timezone) {
        if (cityName != null) {
            return destinationRepository.findByCityNameContainingIgnoreCase(pageable, cityName).map(entity -> {
                var dto = destinationMapper.convertToDestinationDTOEntity(entity);
                return dto;
            });
        } else if(countryName != null) {
            return destinationRepository.findByCountryNameContainingIgnoreCase(pageable, countryName).map(entity -> {
                var dto = destinationMapper.convertToDestinationDTOEntity(entity);
                return dto;
            });
        } else {
            return destinationRepository.findByTimezoneContainingIgnoreCase(pageable, timezone).map(entity -> {
                var dto = destinationMapper.convertToDestinationDTOEntity(entity);
                return dto;
            });
        }
    }

    @Override
    @Transactional
    public void saveDestination(DestinationDTO destinationDTO) {
        destinationRepository.save(destinationMapper.convertToDestinationEntity(destinationDTO));
    }

    @Override
    @Transactional
    public void updateDestinationById(Long id, DestinationDTO destinationDTO) {
        destinationDTO.setId(id);
        destinationRepository.save(destinationMapper.convertToDestinationEntity(destinationDTO));
    }

    @Override
    public DestinationDTO getDestinationById(Long id) {
        return destinationRepository.findById(id).map(entity -> {
            var dto = destinationMapper.convertToDestinationDTOEntity(entity);
            return dto;
        }).orElse(null);
    }

    @Override
    public DestinationDTO getDestinationByAirportCode(Airport airportCode) {
        return destinationRepository.findDestinationByAirportCode(airportCode).map(entity -> {
            var dto = destinationMapper.convertToDestinationDTOEntity(entity);
            return dto;
        }).orElse(null);
    }

    @Override
    public void deleteDestinationById(Long id) {
        destinationRepository.deleteById(id);
    }
}
