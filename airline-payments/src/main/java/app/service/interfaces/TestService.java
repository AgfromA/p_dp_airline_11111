package app.service.interfaces;

import app.entities.account.Test;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TestService {
    Test saveTest(Test test);

    Test updateTest(Long id, Test Test);

    Page<Test> getAllTest(Integer page, Integer size);

    Optional<Test> getTestById(Long id);

}
