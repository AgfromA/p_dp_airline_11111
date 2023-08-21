package app.services.interfaces;

import app.dto.ExampleDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface ExampleService {

    List<ExampleDto> findAll();

    Page<ExampleDto> getPage(int page, int size);

    Optional<ExampleDto> findById(Long id);

    ExampleDto save(ExampleDto exampleDto);

    Optional<ExampleDto> update(Long id, ExampleDto exampleDto);

    Optional<ExampleDto> delete(Long id);
}
