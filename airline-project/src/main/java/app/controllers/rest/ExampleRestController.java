package app.controllers.rest;


import app.controllers.api.rest.ExampleRestApi;
import app.dto.ExampleDto;
import app.services.interfaces.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class ExampleRestController implements ExampleRestApi {

    private final ExampleService exampleService;

    @Override
    public ResponseEntity<Page<ExampleDto>> getPage(Integer page, Integer size) {
        if (page == null || size == null) {
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            return ResponseEntity.noContent().build();
        }

        var examplePage = exampleService.getPage(page, size);
        if (examplePage.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return createPagedResponse(examplePage);
        }
    }

    private ResponseEntity<Page<ExampleDto>> createUnPagedResponse() {
        var examples = exampleService.findAll();
        if (examples.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new PageImpl<>(examples.stream().collect(Collectors.toList())));
    }

    private ResponseEntity<Page<ExampleDto>> createPagedResponse(Page<ExampleDto> examplePage) {
        var exampleDtoPage = new PageImpl<>(
                examplePage.getContent().stream().collect(Collectors.toList()),
                examplePage.getPageable(),
                examplePage.getTotalElements()
        );
        return ResponseEntity.ok(exampleDtoPage);
    }

    @Override
    public ResponseEntity<ExampleDto> get(Long id) {
        return exampleService.findById(id)
                .map(value -> ResponseEntity.ok(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ExampleDto> create(ExampleDto exampleDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(exampleService.save(exampleDto));
    }

    @Override
    public ResponseEntity<ExampleDto> update(Long id, ExampleDto exampleDto) {
        return exampleService.update(id, exampleDto)
                .map(example -> ResponseEntity.ok(exampleDto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        Optional<ExampleDto> example = exampleService.delete(id);
        if (example.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
