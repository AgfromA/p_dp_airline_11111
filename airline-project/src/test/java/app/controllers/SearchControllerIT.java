package app.controllers;

import app.dto.search.Search;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static app.enums.Airport.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-search-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SearchControllerIT extends IntegrationTestBase {

    @Test
    void CheckSearchResult() throws Exception {
        var search = new Search(VKO, OMS, LocalDate.of(2023, 04, 01), null, 1);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void CheckSearchFromIsNull() throws Exception {
        var search = new Search(null, OMS, LocalDate.of(2023, 04, 01),
                LocalDate.of(2023, 04, 01), 1);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void CheckSearchToIsNull() throws Exception {
        var search = new Search(VKO, null, LocalDate.of(2023, 04, 01),
                LocalDate.of(2023, 04, 01), 1);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void CheckSearchFromAndToAreNull() throws Exception {
        var search = new Search(null, null, LocalDate.of(2023, 04, 01),
                LocalDate.of(2023, 04, 01), 1);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void CheckSearchPassengerIsNull() throws Exception {
        var search = new Search(VKO, OMS, LocalDate.of(2023, 04, 01),
                LocalDate.of(2023, 04, 01), null);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void CheckSearchPassengerIsLessOne() throws Exception {
        var search = new Search(VKO, OMS, LocalDate.of(2023, 04, 01),
                LocalDate.of(2023, 04, 01), 0);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void CheckSearchReturnEarlierDeparture() throws Exception {
        var search = new Search(VKO, OMS, LocalDate.of(2023, 04, 05),
                LocalDate.of(2023, 04, 01), 2);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void CheckSearchDepartureDateIsNull() throws Exception {
        var search = new Search(VKO, OMS, null, null, 2);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void CheckSearchNotFound() throws Exception {
        var search = new Search(VKO, OMS, LocalDate.of(1999, 04, 01),
                LocalDate.of(1999, 04, 05), 2);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
