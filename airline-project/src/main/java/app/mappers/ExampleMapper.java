package app.mappers;


import app.dto.ExampleDto;
import app.entities.Example;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExampleMapper {

    ExampleDto toDto(Example example);

    Example toEntity(ExampleDto exampleDto);
}