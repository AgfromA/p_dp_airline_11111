package app.exceptions;

import app.controllers.IntegrationTestBase;
import app.controllers.rest.AccountRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RuntimeExceptionHandlerTestIT extends IntegrationTestBase {
    @Mock
    private AccountRestController accountRestController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountRestController)
                .setControllerAdvice(ValidationExceptionHandler.class).build();
    }

//    @Test
    public void runtimeExceptionHandlerTestIT() throws Exception {
        Long id = 1L;

        when(accountRestController.getAccountDTOById(id)).thenThrow(new RuntimeException("Runtime Exception"));

        mockMvc.perform(
                        get("http://localhost:8080/api/accounts/{id}", id))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }
}
