package app.controllers.rest;

import app.controllers.api.TestControllerApi;
import app.dto.TestDTO;
import app.mappers.TestMapper;
import app.service.interfaces.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController implements TestControllerApi {

    private final TestService TestService;
    private final TestMapper testMapper;
    @Override
    public ResponseEntity<Page> getAllTestsPages(Integer page, Integer size) {
        log.info("getAll: get all Accounts");
        var tests = TestService.getAllTest(page, size);
        return tests.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(tests, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TestDTO> getTestDTOById(Long id) {
        log.info("getById: get Account by id. id = {}", id);
        var test = TestService.getTestById(id);
        return test.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(new TestDTO(test.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TestDTO> createTestDTO(TestDTO testDTO)  {
        log.info("create: create new Account with email={}", testDTO.getEmail());
        return ResponseEntity.ok(new TestDTO(TestService.saveTest(testMapper.convertToTest(testDTO))));
    }

    @Override
    public ResponseEntity<TestDTO> updateTestDTOById(Long id, TestDTO testDTO)  {
        log.info("update: update Account with id = {}", id);
        return new ResponseEntity<>(new TestDTO( TestService.updateTest(id,
                testMapper.convertToTest(testDTO))), HttpStatus.OK);
    }
}
