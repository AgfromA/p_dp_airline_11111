package app.mappers;

import app.dto.DestinationDTO;
import app.entities.Destination;
import app.enums.Airport;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

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
        assertEquals(destinationDTO.getAirportCode().getAirportName(), destination.getAirportName());
        assertEquals(destinationDTO.getAirportCode().getCity(), destination.getCityName());
        assertEquals(destinationDTO.getTimezone(), destination.getTimezone());
        assertEquals(destinationDTO.getAirportCode().getCountry(), destination.getCountryName());
    }

    @Test
    public void testConvertToDestinationDTOList() {
        List<Destination> destinationList = new ArrayList<>();

        Destination destinationOne = new Destination();
        destinationOne.setId(1L);
        destinationOne.setAirportCode(Airport.AAQ);
        destinationOne.setAirportName("Витязево");
        destinationOne.setCityName("Анапа");
        destinationOne.setTimezone("UTC+3");
        destinationOne.setCountryName("Россия");

        Destination destinationTwo = new Destination();
        destinationTwo.setId(2L);
        destinationTwo.setAirportCode(Airport.ABA);
        destinationTwo.setAirportName("Абакан");
        destinationTwo.setCityName("Абакан");
        destinationTwo.setTimezone("UTC+3");
        destinationTwo.setCountryName("Россия");

        destinationList.add(destinationOne);
        destinationList.add(destinationTwo);

        List<DestinationDTO> destinationDTOList = destinationMapper.convertToDestinationDTOList(destinationList);
        assertEquals(destinationList.size(), destinationDTOList.size());
        assertEquals(destinationList.get(0).getId(), destinationDTOList.get(0).getId());
        assertEquals(destinationList.get(0).getAirportCode(), destinationDTOList.get(0).getAirportCode());
        assertEquals(destinationList.get(0).getAirportName(), destinationDTOList.get(0).getAirportCode().getAirportName());
        assertEquals(destinationList.get(0).getCityName(), destinationDTOList.get(0).getAirportCode().getCity());
        assertEquals(destinationList.get(0).getTimezone(), destinationDTOList.get(0).getTimezone());
        assertEquals(destinationList.get(0).getCountryName(), destinationDTOList.get(0).getAirportCode().getCountry());

        assertEquals(destinationList.get(1).getId(), destinationDTOList.get(1).getId());
        assertEquals(destinationList.get(1).getAirportCode(), destinationDTOList.get(1).getAirportCode());
        assertEquals(destinationList.get(1).getAirportName(), destinationDTOList.get(1).getAirportCode().getAirportName());
        assertEquals(destinationList.get(1).getCityName(), destinationDTOList.get(1).getAirportCode().getCity());
        assertEquals(destinationList.get(1).getTimezone(), destinationDTOList.get(1).getTimezone());
        assertEquals(destinationList.get(1).getCountryName(), destinationDTOList.get(1).getAirportCode().getCountry());
    }

    @Test
    public void testConvertToDestinationEntityList() {
        List<DestinationDTO> destinationDTOList = new ArrayList<>();
        DestinationDTO destinationDTOOne = new DestinationDTO();
        destinationDTOOne.setId(1L);
        destinationDTOOne.setAirportCode(Airport.AAQ);
        destinationDTOOne.setTimezone("UTC+3");
        DestinationDTO destinationDTOTwo = new DestinationDTO();
        destinationDTOTwo.setId(2L);
        destinationDTOTwo.setAirportCode(Airport.ABA);
        destinationDTOTwo.setTimezone("UTC+3");

        destinationDTOList.add(destinationDTOOne);
        destinationDTOList.add(destinationDTOTwo);

        List<Destination> destinationList = destinationMapper.convertToDestinationEntityList(destinationDTOList);
        assertEquals(destinationDTOList.size(), destinationList.size());
        assertEquals(destinationDTOList.get(0).getId(), destinationList.get(0).getId());
        assertEquals(destinationDTOList.get(0).getAirportCode(), destinationList.get(0).getAirportCode());
        assertEquals(destinationDTOList.get(0).getAirportCode().getAirportName(), destinationList.get(0).getAirportName());
        assertEquals(destinationDTOList.get(0).getAirportCode().getCity(), destinationList.get(0).getCityName());
        assertEquals(destinationDTOList.get(0).getTimezone(), destinationList.get(0).getTimezone());
        assertEquals(destinationDTOList.get(0).getAirportCode().getCountry(), destinationList.get(0).getCountryName());

        assertEquals(destinationDTOList.get(1).getId(), destinationList.get(1).getId());
        assertEquals(destinationDTOList.get(1).getAirportCode(), destinationList.get(1).getAirportCode());
        assertEquals(destinationDTOList.get(1).getAirportCode().getAirportName(), destinationList.get(1).getAirportName());
        assertEquals(destinationDTOList.get(1).getAirportCode().getCity(), destinationList.get(1).getCityName());
        assertEquals(destinationDTOList.get(1).getTimezone(), destinationList.get(1).getTimezone());
        assertEquals(destinationDTOList.get(1).getAirportCode().getCountry(), destinationList.get(1).getCountryName());
    }
}