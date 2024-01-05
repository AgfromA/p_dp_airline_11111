package app.mappers;

import app.dto.TimezoneDTO;
import app.entities.Timezone;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimezoneMapperTest {
    private final TimezoneMapper timezoneMapper = Mappers.getMapper(TimezoneMapper.class);

    @Test
    public void testConvertToTimezoneDTO() {
        Timezone timezone = new Timezone();
        timezone.setId(1L);
        timezone.setGmt("+3");
        timezone.setGmtWinter("+4");
        timezone.setCityName("Moscow");
        timezone.setCountryName("Russia");

        TimezoneDTO timezoneDTO = timezoneMapper.convertToTimezoneDTO(timezone);

        assertEquals(timezone.getId(), timezoneDTO.getId());
        assertEquals(timezone.getGmt(), timezoneDTO.getGmt());
        assertEquals(timezone.getGmtWinter(), timezoneDTO.getGmtWinter());
        assertEquals(timezone.getCityName(), timezoneDTO.getCityName());
        assertEquals(timezone.getCountryName(), timezoneDTO.getCountryName());
    }

    @Test
    public void testConvertToTimezone() {
        TimezoneDTO timezoneDTO = new TimezoneDTO();
        timezoneDTO.setId(1L);
        timezoneDTO.setGmt("+3");
        timezoneDTO.setGmtWinter("+4");
        timezoneDTO.setCityName("Moscow");
        timezoneDTO.setCountryName("Russia");

        Timezone timezone = timezoneMapper.convertToTimezone(timezoneDTO);

        assertEquals(timezone.getId(), timezoneDTO.getId());
        assertEquals(timezone.getGmt(), timezoneDTO.getGmt());
        assertEquals(timezone.getGmtWinter(), timezoneDTO.getGmtWinter());
        assertEquals(timezone.getCityName(), timezoneDTO.getCityName());
        assertEquals(timezone.getCountryName(), timezoneDTO.getCountryName());
    }

    @Test
    public void testConvertToTimezoneDTOList() {
        List<Timezone> timezoneList = new ArrayList<>();
        Timezone timezoneOne = new Timezone();
        timezoneOne.setId(1L);
        timezoneOne.setGmt("+3");
        timezoneOne.setGmtWinter("+4");
        timezoneOne.setCityName("Moscow");
        timezoneOne.setCountryName("Russia");

        Timezone timezoneTwo = new Timezone();
        timezoneTwo.setId(2L);
        timezoneTwo.setGmt("+4");
        timezoneTwo.setGmtWinter("+5");
        timezoneTwo.setCityName("Orel");
        timezoneTwo.setCountryName("Russia");

        timezoneList.add(timezoneOne);
        timezoneList.add(timezoneTwo);

        List<TimezoneDTO> timezoneDTOList = timezoneMapper.convertToTimezoneDTOList(timezoneList);

        assertEquals(timezoneList.size(), timezoneDTOList.size());
        assertEquals(timezoneList.get(0).getId(), timezoneDTOList.get(0).getId());
        assertEquals(timezoneList.get(0).getGmt(), timezoneDTOList.get(0).getGmt());
        assertEquals(timezoneList.get(0).getGmtWinter(), timezoneDTOList.get(0).getGmtWinter());
        assertEquals(timezoneList.get(0).getCityName(), timezoneDTOList.get(0).getCityName());
        assertEquals(timezoneList.get(0).getCountryName(), timezoneDTOList.get(0).getCountryName());

        assertEquals(timezoneList.get(1).getId(), timezoneDTOList.get(1).getId());
        assertEquals(timezoneList.get(1).getGmt(), timezoneDTOList.get(1).getGmt());
        assertEquals(timezoneList.get(1).getGmtWinter(), timezoneDTOList.get(1).getGmtWinter());
        assertEquals(timezoneList.get(1).getCityName(), timezoneDTOList.get(1).getCityName());
        assertEquals(timezoneList.get(1).getCountryName(), timezoneDTOList.get(1).getCountryName());
    }

    @Test
    public void testConvertToTimezoneList() {
        List<TimezoneDTO> timezoneDTOList = new ArrayList<>();
        TimezoneDTO timezoneDTOOne = new TimezoneDTO();
        timezoneDTOOne.setId(1L);
        timezoneDTOOne.setGmt("+3");
        timezoneDTOOne.setGmtWinter("+4");
        timezoneDTOOne.setCityName("Moscow");
        timezoneDTOOne.setCountryName("Russia");

        TimezoneDTO timezoneDTOTwo = new TimezoneDTO();
        timezoneDTOTwo.setId(2L);
        timezoneDTOTwo.setGmt("+4");
        timezoneDTOTwo.setGmtWinter("+5");
        timezoneDTOTwo.setCityName("Orel");
        timezoneDTOTwo.setCountryName("Russia");

        timezoneDTOList.add(timezoneDTOOne);
        timezoneDTOList.add(timezoneDTOTwo);

        List<Timezone> timezoneList = timezoneMapper.convertToTimezoneList(timezoneDTOList);

        assertEquals(timezoneDTOList.size(), timezoneList.size());
        assertEquals(timezoneDTOList.get(0).getId(), timezoneList.get(0).getId());
        assertEquals(timezoneDTOList.get(0).getGmt(), timezoneList.get(0).getGmt());
        assertEquals(timezoneDTOList.get(0).getGmtWinter(), timezoneList.get(0).getGmtWinter());
        assertEquals(timezoneDTOList.get(0).getCityName(), timezoneList.get(0).getCityName());
        assertEquals(timezoneDTOList.get(0).getCountryName(), timezoneList.get(0).getCountryName());

        assertEquals(timezoneDTOList.get(1).getId(), timezoneList.get(1).getId());
        assertEquals(timezoneDTOList.get(1).getGmt(), timezoneList.get(1).getGmt());
        assertEquals(timezoneDTOList.get(1).getGmtWinter(), timezoneList.get(1).getGmtWinter());
        assertEquals(timezoneDTOList.get(1).getCityName(), timezoneList.get(1).getCityName());
        assertEquals(timezoneDTOList.get(1).getCountryName(), timezoneList.get(1).getCountryName());
    }
}
