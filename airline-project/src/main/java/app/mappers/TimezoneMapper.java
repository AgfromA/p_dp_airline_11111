package app.mappers;

import app.dto.TimezoneDTO;
import app.entities.Timezone;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimezoneMapper {

    TimezoneDTO convertToTimezoneDTO(Timezone timezone);

    Timezone convertToTimezone(TimezoneDTO timezoneDTO);

    List<TimezoneDTO> convertToTimezoneDTOList(List<Timezone> timezoneList);

    List<Timezone> convertToTimezoneList(List<TimezoneDTO> timezoneDTOList);
}
