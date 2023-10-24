package app.controllers;

import app.entities.account.search.Search;
import app.enums.Airport;
import app.mappers.DestinationMapper;
import app.services.interfaces.DestinationService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-search-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class SearchControllerIT extends IntegrationTestBase {

    @Test
    void CheckSearchResultNotFound() throws Exception {
        Long id = 200L;
        mockMvc.perform(get("http://localhost:8080/api/search/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void CreateSearchResultCreate() throws Exception {
        var from = Airport.VKO;
        var to = Airport.OMS;
        var search = new Search(from, to, LocalDate.of(2023, 04, 01), null, 1);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    void CheckSearchNotFound() throws Exception {
        var from = Airport.VKO;
        var to = Airport.OMS;
        var search = new Search(from, to, LocalDate.of(1999, 12, 01), null, 1);
        mockMvc.perform(post("http://localhost:8080/api/search")
                        .content(objectMapper.writeValueAsString(search))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}
