package app.mappers;

import app.dto.TimezoneDto;
import app.entities.Timezone;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimezoneMapper {

    TimezoneDto toDto(Timezone timezone);

    Timezone toEntity(TimezoneDto timezoneDto);

    List<TimezoneDto> toDtoList(List<Timezone> timezoneList);

    List<Timezone> toEntityList(List<TimezoneDto> timezoneDtoList);
}