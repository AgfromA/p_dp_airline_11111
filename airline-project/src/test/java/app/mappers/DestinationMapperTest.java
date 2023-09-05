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
        destination.setAirportName("Vityazevo");
        destination.setCityName("Anapa");
        destination.setTimezone("UTC+3");
        destination.setCountryName("Russia");

        // Convert the Destination object to DestinationDTO using the mapper
        DestinationDTO destinationDTO = destinationMapper.convertToDestinationDTOEntity(destination);

        // Verify the mapping
        assertEquals(destination.getId(), destinationDTO.getId());
        assertEquals(destination.getAirportCode(), destinationDTO.getAirportCode());
        assertEquals(destination.getAirportName(), destinationDTO.getAirportName());
        assertEquals(destination.getCityName(), destinationDTO.getCityName());
        assertEquals(destination.getTimezone(), destinationDTO.getTimezone());
        assertEquals(destination.getCountryName(), destinationDTO.getCountryName());
    }
}