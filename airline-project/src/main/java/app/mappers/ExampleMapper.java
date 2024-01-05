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

    List<ExampleDto> convertToExamleDTOList (List<Example> examples);
    List<Example> convertToExamleList (List<ExampleDto> exampleDtos);
}