package app.mappers;

import app.dto.TestDTO;
import app.entities.account.Test;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestMapper {

    public Test convertToTest(TestDTO testDTO) {
        var test = new Test();
        test.setFirstName(testDTO.getFirstName());
        test.setLastName(testDTO.getLastName());
        test.setEmail(testDTO.getEmail());
        test.setPassword(testDTO.getPassword());
        test.setPassword(test.getPassword());

        return test;
    }
}
