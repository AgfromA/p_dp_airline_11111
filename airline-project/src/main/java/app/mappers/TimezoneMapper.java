package app.mappers;

import app.dto.TimezoneDto;
import app.entities.Timezone;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimezoneMapper {

    TimezoneDto convertToTimezoneDto(Timezone timezone);

    Timezone convertToTimezone(TimezoneDto timezoneDto);

    List<TimezoneDto> convertToTimezoneDtoList(List<Timezone> timezoneList);

    List<Timezone> convertToTimezoneList(List<TimezoneDto> timezoneDtoList);
}