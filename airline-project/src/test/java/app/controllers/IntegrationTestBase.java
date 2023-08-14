package app.controllers;

import app.initializer.PostgresSqlContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@TestPropertySource("/application-test.yml")
@SpringBootTest
@ContextConfiguration(initializers = {
        PostgresSqlContainer.Initializer.class
})
@WithMockUser(roles = "ADMIN")
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {

    @BeforeAll
    static void init() {
        PostgresSqlContainer.container.start();
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}