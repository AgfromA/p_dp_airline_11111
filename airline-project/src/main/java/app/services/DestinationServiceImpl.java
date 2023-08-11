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
    public Page<Destination> getAllDestinations(Integer page, Integer size) {
        return destinationRepository.findAll(PageRequest.of(page, size));
//    public Page<DestinationDTO> getAllDestinations(Pageable pageable) {
//        return destinationRepository.findAll(pageable).map(entity -> {
//            var dto = destinationMapper.convertToDestinationDTOEntity(entity);
//            return dto;
//        });
//    }

    @Override
    public Page<Destination> getDestinationByNameAndTimezone(Integer page, Integer size, String cityName, String countryName, String timezone) {
        if (cityName != null) {
            return destinationRepository.findByCityNameContainingIgnoreCase(PageRequest.of(page, size), cityName);
        } else if(countryName != null) {
            return destinationRepository.findByCountryNameContainingIgnoreCase(PageRequest.of(page, size), countryName);
        } else {
            return destinationRepository.findByTimezoneContainingIgnoreCase(PageRequest.of(page, size), timezone);
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
