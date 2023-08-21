package app.service;

import app.entities.account.Test;
import app.repositories.TestRepository;
import app.service.interfaces.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final PasswordEncoder encoder;
    @Override
    public Test saveTest(Test Test) {
        Test.setPassword(encoder.encode(Test.getPassword()));
        return testRepository.saveAndFlush(Test);
    }

    @Override
    public Test updateTest(Long id, Test test) {
        var editTest = testRepository.getTestById(id);
        if (!test.getPassword().equals(editTest.getPassword())) {
            editTest.setPassword(encoder.encode(test.getPassword()));
        }
        editTest.setEmail(test.getEmail());
        editTest.setFirstName(test.getFirstName());
        editTest.setLastName(test.getLastName());
        return testRepository.saveAndFlush(editTest);
    }

    @Override
    public Page<Test> getAllTest(Integer page, Integer size) {
        return testRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Optional<Test> getTestById(Long id) {
        return testRepository.findById(id);
    }
}
