package app.mappers;

import app.dto.DestinationDto;
import app.entities.Destination;
import app.enums.Airport;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static app.enums.Airport.SVX;
import static app.enums.Airport.VKO;
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

        // Convert the Destination object to DestinationDto using the mapper
        DestinationDto destinationDTO = destinationMapper.toDto(destination);

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
        DestinationDto destinationDTO = new DestinationDto();
        destinationDTO.setId(3L);
        destinationDTO.setAirportCode(VKO);
        destinationDTO.setTimezone("+3");
        destinationDTO.setCityName("Москва");
        destinationDTO.setCountryName("Россия");
        destinationDTO.setAirportName("Внуково");

        Destination destination = destinationMapper.toEntity(destinationDTO);

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

        List<DestinationDto> destinationDtoList = destinationMapper.toDtoList(destinationList);
        assertEquals(destinationList.size(), destinationDtoList.size());
        assertEquals(destinationList.get(0).getId(), destinationDtoList.get(0).getId());
        assertEquals(destinationList.get(0).getAirportCode(), destinationDtoList.get(0).getAirportCode());
        assertEquals(destinationList.get(0).getAirportName(), destinationDtoList.get(0).getAirportCode().getAirportName());
        assertEquals(destinationList.get(0).getCityName(), destinationDtoList.get(0).getAirportCode().getCity());
        assertEquals(destinationList.get(0).getTimezone(), destinationDtoList.get(0).getTimezone());
        assertEquals(destinationList.get(0).getCountryName(), destinationDtoList.get(0).getAirportCode().getCountry());

        assertEquals(destinationList.get(1).getId(), destinationDtoList.get(1).getId());
        assertEquals(destinationList.get(1).getAirportCode(), destinationDtoList.get(1).getAirportCode());
        assertEquals(destinationList.get(1).getAirportName(), destinationDtoList.get(1).getAirportCode().getAirportName());
        assertEquals(destinationList.get(1).getCityName(), destinationDtoList.get(1).getAirportCode().getCity());
        assertEquals(destinationList.get(1).getTimezone(), destinationDtoList.get(1).getTimezone());
        assertEquals(destinationList.get(1).getCountryName(), destinationDtoList.get(1).getAirportCode().getCountry());
    }

    @Test
    public void testConvertToDestinationEntityList() {
        List<DestinationDto> destinationDtoList = new ArrayList<>();

        DestinationDto destinationDtoOne = new DestinationDto();
        destinationDtoOne.setId(3L);
        destinationDtoOne.setAirportCode(VKO);
        destinationDtoOne.setTimezone("GMT+3");
        destinationDtoOne.setCityName("Москва");
        destinationDtoOne.setCountryName("Россия");
        destinationDtoOne.setAirportName("Внуково");

        DestinationDto destinationDtoTwo = new DestinationDto();
        destinationDtoTwo.setId(2L);
        destinationDtoTwo.setAirportCode(SVX);
        destinationDtoTwo.setTimezone("GMT +5");
        destinationDtoTwo.setCityName("Екатеринбург");
        destinationDtoTwo.setCountryName("Россия");
        destinationDtoTwo.setAirportName("Кольцово");

        destinationDtoList.add(destinationDtoOne);
        destinationDtoList.add(destinationDtoTwo);

        List<Destination> destinationList = destinationMapper.toEntityList(destinationDtoList);
        assertEquals(destinationDtoList.size(), destinationList.size());
        assertEquals(destinationDtoList.get(0).getId(), destinationList.get(0).getId());
        assertEquals(destinationDtoList.get(0).getAirportCode(), destinationList.get(0).getAirportCode());
        assertEquals(destinationDtoList.get(0).getAirportCode().getAirportName(), destinationList.get(0).getAirportName());
        assertEquals(destinationDtoList.get(0).getAirportCode().getCity(), destinationList.get(0).getCityName());
        assertEquals(destinationDtoList.get(0).getTimezone(), destinationList.get(0).getTimezone());
        assertEquals(destinationDtoList.get(0).getAirportCode().getCountry(), destinationList.get(0).getCountryName());

        assertEquals(destinationDtoList.get(1).getId(), destinationList.get(1).getId());
        assertEquals(destinationDtoList.get(1).getAirportCode(), destinationList.get(1).getAirportCode());
        assertEquals(destinationDtoList.get(1).getAirportCode().getAirportName(), destinationList.get(1).getAirportName());
        assertEquals(destinationDtoList.get(1).getAirportCode().getCity(), destinationList.get(1).getCityName());
        assertEquals(destinationDtoList.get(1).getTimezone(), destinationList.get(1).getTimezone());
        assertEquals(destinationDtoList.get(1).getAirportCode().getCountry(), destinationList.get(1).getCountryName());
    }
}