package app.exceptions;

import app.controllers.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-destination-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EmptyResultDataAccessExceptionHandlerTestIT extends IntegrationTestBase {

    @Test
    void shouldDeleteDestinationById() throws Exception {
        Long id = 0L;
        mockMvc.perform(delete("http://localhost:8080/api/destinations/{id}", id))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}