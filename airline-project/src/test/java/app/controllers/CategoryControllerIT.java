package app.controllers;

import app.entities.Category;
import app.enums.CategoryType;
import app.services.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@Sql(value = {"/sqlQuery/create-aircraftCategorySeat-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class CategoryControllerIT extends IntegrationTestBase {

    @Autowired
    private CategoryService categoryService;

    @Test
    void shouldGetAllCategory() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/categories"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetCategoryByCategoryType() throws Exception {
        CategoryType categoryType = CategoryType.ECONOMY;
        mockMvc.perform(get("http://localhost:8080/api/categories/{category_type}", categoryType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categoryService.getCategoryByType(categoryType))));
    }

    public String convertPageToJson(Page page) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Category> categories = page.getContent();
        int totalPages = page.getTotalPages();
        long totalElements = page.getTotalElements();
        boolean last = page.isLast();

        Map<String, Object> map = new HashMap<>();
        map.put("content", categories);
        map.put("totalPages", totalPages);
        map.put("totalElements", totalElements);
        map.put("last", last);

        return mapper.writeValueAsString(map);
    }
    @Test
    void shouldGetPageCategory() throws Exception {
        var pageable = PageRequest.of(0, 2);
        mockMvc.perform(get("http://localhost:8080/api/categories?page=0&size=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(convertPageToJson(categoryService.getPage(pageable.getPageNumber(), pageable.getPageSize()))));
    }
}
