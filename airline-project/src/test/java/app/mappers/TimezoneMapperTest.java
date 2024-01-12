package app.mappers;

import app.dto.TimezoneDto;
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

        TimezoneDto timezoneDto = timezoneMapper.toDto(timezone);

        assertEquals(timezone.getId(), timezoneDto.getId());
        assertEquals(timezone.getGmt(), timezoneDto.getGmt());
        assertEquals(timezone.getGmtWinter(), timezoneDto.getGmtWinter());
        assertEquals(timezone.getCityName(), timezoneDto.getCityName());
        assertEquals(timezone.getCountryName(), timezoneDto.getCountryName());
    }

    @Test
    public void testConvertToTimezone() {
        TimezoneDto timezoneDto = new TimezoneDto();
        timezoneDto.setId(1L);
        timezoneDto.setGmt("+3");
        timezoneDto.setGmtWinter("+4");
        timezoneDto.setCityName("Moscow");
        timezoneDto.setCountryName("Russia");

        Timezone timezone = timezoneMapper.toEntity(timezoneDto);

        assertEquals(timezone.getId(), timezoneDto.getId());
        assertEquals(timezone.getGmt(), timezoneDto.getGmt());
        assertEquals(timezone.getGmtWinter(), timezoneDto.getGmtWinter());
        assertEquals(timezone.getCityName(), timezoneDto.getCityName());
        assertEquals(timezone.getCountryName(), timezoneDto.getCountryName());
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

        List<TimezoneDto> timezoneDtoList = timezoneMapper.toDtoList(timezoneList);

        assertEquals(timezoneList.size(), timezoneDtoList.size());
        assertEquals(timezoneList.get(0).getId(), timezoneDtoList.get(0).getId());
        assertEquals(timezoneList.get(0).getGmt(), timezoneDtoList.get(0).getGmt());
        assertEquals(timezoneList.get(0).getGmtWinter(), timezoneDtoList.get(0).getGmtWinter());
        assertEquals(timezoneList.get(0).getCityName(), timezoneDtoList.get(0).getCityName());
        assertEquals(timezoneList.get(0).getCountryName(), timezoneDtoList.get(0).getCountryName());

        assertEquals(timezoneList.get(1).getId(), timezoneDtoList.get(1).getId());
        assertEquals(timezoneList.get(1).getGmt(), timezoneDtoList.get(1).getGmt());
        assertEquals(timezoneList.get(1).getGmtWinter(), timezoneDtoList.get(1).getGmtWinter());
        assertEquals(timezoneList.get(1).getCityName(), timezoneDtoList.get(1).getCityName());
        assertEquals(timezoneList.get(1).getCountryName(), timezoneDtoList.get(1).getCountryName());
    }

    @Test
    public void testConvertToTimezoneList() {
        List<TimezoneDto> timezoneDtoList = new ArrayList<>();
        TimezoneDto timezoneDtoOne = new TimezoneDto();
        timezoneDtoOne.setId(1L);
        timezoneDtoOne.setGmt("+3");
        timezoneDtoOne.setGmtWinter("+4");
        timezoneDtoOne.setCityName("Moscow");
        timezoneDtoOne.setCountryName("Russia");

        TimezoneDto timezoneDtoTwo = new TimezoneDto();
        timezoneDtoTwo.setId(2L);
        timezoneDtoTwo.setGmt("+4");
        timezoneDtoTwo.setGmtWinter("+5");
        timezoneDtoTwo.setCityName("Orel");
        timezoneDtoTwo.setCountryName("Russia");

        timezoneDtoList.add(timezoneDtoOne);
        timezoneDtoList.add(timezoneDtoTwo);

        List<Timezone> timezoneList = timezoneMapper.toEntityList(timezoneDtoList);

        assertEquals(timezoneDtoList.size(), timezoneList.size());
        assertEquals(timezoneDtoList.get(0).getId(), timezoneList.get(0).getId());
        assertEquals(timezoneDtoList.get(0).getGmt(), timezoneList.get(0).getGmt());
        assertEquals(timezoneDtoList.get(0).getGmtWinter(), timezoneList.get(0).getGmtWinter());
        assertEquals(timezoneDtoList.get(0).getCityName(), timezoneList.get(0).getCityName());
        assertEquals(timezoneDtoList.get(0).getCountryName(), timezoneList.get(0).getCountryName());

        assertEquals(timezoneDtoList.get(1).getId(), timezoneList.get(1).getId());
        assertEquals(timezoneDtoList.get(1).getGmt(), timezoneList.get(1).getGmt());
        assertEquals(timezoneDtoList.get(1).getGmtWinter(), timezoneList.get(1).getGmtWinter());
        assertEquals(timezoneDtoList.get(1).getCityName(), timezoneList.get(1).getCityName());
        assertEquals(timezoneDtoList.get(1).getCountryName(), timezoneList.get(1).getCountryName());
    }
}
