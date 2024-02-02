package app.controllers.rest;

import app.controllers.api.rest.ExampleRestApi;
import app.dto.ExampleDto;
import app.services.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ExampleRestController implements ExampleRestApi {

    private final ExampleService exampleService;

    @Override
    public ResponseEntity<Page<ExampleDto>> getAllExamples(Integer page, Integer size) {
        log.info("getAllExamples:");
        if (page == null || size == null) {
            return createUnPagedResponse();
        }
        var examples = exampleService.getPage(page, size);
        return examples.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(examples, HttpStatus.OK);
    }

    private ResponseEntity<Page<ExampleDto>> createUnPagedResponse() {
        var examples = exampleService.findAll();
        if (examples.isEmpty()) {
            log.info("getAllExamples: not found Example");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAllExamples: found {} Example", examples.size());
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(examples)));
        }
    }

    @Override
    public ResponseEntity<ExampleDto> get(Long id) {
        return exampleService.findById(id)
                .map(value -> ResponseEntity.ok(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ExampleDto> create(ExampleDto exampleDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exampleService.save(exampleDto));
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