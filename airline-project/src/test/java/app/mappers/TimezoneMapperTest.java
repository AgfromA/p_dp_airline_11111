package app.mappers;

import app.dto.TimezoneDTO;
import app.entities.Timezone;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

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
}
