package app.controllers.rest;

import app.controllers.api.rest.ExampleRestApi;
import app.dto.ExampleDto;
import app.services.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class ExampleRestController implements ExampleRestApi {

    private final ExampleService exampleService;

    @Override
    public ResponseEntity<List<ExampleDto>> getAllExamples(Integer page, Integer size) {
        log.info("getAll: get  Examples");
        if (page == null || size == null) {
            log.info("getAll: get all List Example");
            return createUnPagedResponse();
        }
        if (page < 0 || size < 1) {
            log.info("getAll: no correct data");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var examplePage = exampleService.getPage(page, size);

        return examplePage.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(examplePage.getContent(), HttpStatus.OK);
    }

    private ResponseEntity<List<ExampleDto>> createUnPagedResponse() {
        var examples = exampleService.findAll();
        if (examples.isEmpty()) {
            log.info("getAll: not found Example");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("getAll: found {} Example", examples.size());
            return new ResponseEntity<>(examples, HttpStatus.OK);
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