package app.mappers;

import app.dto.DestinationDTO;
import app.entities.Destination;
import app.enums.Airport;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DestinationMapperTest {

    private final DestinationMapper destinationMapper = Mappers.getMapper(DestinationMapper.class);

    @Test
    public void testConvertToDestinationDTOEntity() {
        // Create a Destination object
        Destination destination = new Destination();
        destination.setId(1L);
        destination.setAirportCode(Airport.AAQ);
        destination.setAirportName("Витязево");
        destination.setCityName("Анапа");
        destination.setTimezone("UTC+3");
        destination.setCountryName("Россия");

        // Convert the Destination object to DestinationDTO using the mapper
        DestinationDTO destinationDTO = destinationMapper.convertToDestinationDTOEntity(destination);

        // Verify the mapping
        assertEquals(destination.getId(), destinationDTO.getId());
        assertEquals(destination.getAirportCode(), destinationDTO.getAirportCode());
        assertEquals(destination.getAirportName(), destinationDTO.getAirportCode().getAirportName());
        assertEquals(destination.getCityName(), destinationDTO.getAirportCode().getCity());
        assertEquals(destination.getTimezone(), destinationDTO.getTimezone());
        assertEquals(destination.getCountryName(), destinationDTO.getAirportCode().getCountry());
    }

    @Test
    public void testConvertToDestinationEntity() {
        // Create a Destination object
        DestinationDTO destinationDTO = new DestinationDTO();
        destinationDTO.setId(1L);
        destinationDTO.setAirportCode(Airport.AAQ);
        destinationDTO.setTimezone("UTC+3");

        Destination destination = destinationMapper.convertToDestinationEntity(destinationDTO);

        assertEquals(destinationDTO.getId(), destination.getId());
        assertEquals(destinationDTO.getAirportCode(), destination.getAirportCode());
        assertEquals(destinationDTO.getAirportCode().getAirportName(),destination.getAirportName());
        assertEquals(destinationDTO.getAirportCode().getCity(), destination.getCityName());
        assertEquals(destinationDTO.getTimezone(), destination.getTimezone());
        assertEquals(destinationDTO.getAirportCode().getCountry(), destination.getCountryName());
    }
}