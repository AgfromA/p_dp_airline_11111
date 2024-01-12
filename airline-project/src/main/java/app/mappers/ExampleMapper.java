package app.mappers;

import app.dto.ExampleDto;
import app.entities.Example;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExampleMapper {

    ExampleDto toDto(Example example);

    Example toEntity(ExampleDto exampleDto);

    List<ExampleDto> toDtoList(List<Example> examples);

    List<Example> toEntityList(List<ExampleDto> exampleDtos);
}