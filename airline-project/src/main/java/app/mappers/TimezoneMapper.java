package app.mappers;

import app.dto.TimezoneDTO;
import app.entities.Timezone;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimezoneMapper {

    TimezoneDTO convertToTimezoneDTO(Timezone timezone);

    Timezone convertToTimezone(TimezoneDTO timezoneDTO);
}
