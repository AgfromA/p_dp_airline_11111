package app.mappers;

import app.dto.ExampleDto;
import app.entities.Example;
import app.services.interfaces.ExampleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class ExampleMapperTest {
    private ExampleMapper exampleMapper = Mappers.getMapper(ExampleMapper.class);
    @Mock
    private ExampleService exampleServiceMock = Mockito.mock(ExampleService.class);

    @Test
    public void shouldConvertExampleToExampleDTO() throws Exception {
        Example example = new Example();
        example.setId(100L);
        example.setExampleText("Test");

        ExampleDto exampleDto = exampleMapper.toDto(example);
        Assertions.assertEquals(example.getId(), exampleDto.getId());
        Assertions.assertEquals(example.getExampleText(), exampleDto.getExampleText());
    }

    @Test
    public void shouldConvertExampleDTOToExample() throws Exception {
        ExampleDto exampleDto = new ExampleDto();
        exampleDto.setId(100L);
        exampleDto.setExampleText("Test");

        Example example = exampleMapper.toEntity(exampleDto);
        Assertions.assertEquals(exampleDto.getId(), example.getId());
        Assertions.assertEquals(exampleDto.getExampleText(), example.getExampleText());
    }

    @Test
    public void shouldConvertExampleListToExampleDTOList() throws Exception {
        List<Example> examples = new ArrayList<>();
        Example example1 = new Example();
        example1.setId(100L);
        example1.setExampleText("Test");
        Example example2 = new Example();
        example2.setId(200L);
        example2.setExampleText("Test2");
        examples.add(example1);
        examples.add(example2);

        List<ExampleDto> exampleDtos = exampleMapper.convertToExamleDTOList(examples);
        Assertions.assertEquals(examples.size(), exampleDtos.size());
        Assertions.assertEquals(examples.get(0).getId(), exampleDtos.get(0).getId());
        Assertions.assertEquals(examples.get(0).getExampleText(), exampleDtos.get(0).getExampleText());
        Assertions.assertEquals(examples.get(1).getId(), exampleDtos.get(1).getId());
        Assertions.assertEquals(examples.get(1).getExampleText(), exampleDtos.get(1).getExampleText());
    }

    @Test
    public void shouldConvertExampleDTOListToExampleList() throws Exception {
        List<ExampleDto> exampleDtos = new ArrayList<>();
        ExampleDto exampleDto1 = new ExampleDto();
        exampleDto1.setId(100L);
        exampleDto1.setExampleText("Test");
        ExampleDto exampleDto2 = new ExampleDto();
        exampleDto2.setId(200L);
        exampleDto2.setExampleText("Test2");
        exampleDtos.add(exampleDto1);
        exampleDtos.add(exampleDto2);

        List<Example> examples = exampleMapper.convertToExamleList(exampleDtos);
        Assertions.assertEquals(exampleDtos.size(), examples.size());
        Assertions.assertEquals(exampleDtos.get(0).getId(), examples.get(0).getId());
        Assertions.assertEquals(exampleDtos.get(0).getExampleText(), examples.get(0).getExampleText());
        Assertions.assertEquals(exampleDtos.get(1).getId(), examples.get(1).getId());
        Assertions.assertEquals(exampleDtos.get(1).getExampleText(), examples.get(1).getExampleText());
    }

}
