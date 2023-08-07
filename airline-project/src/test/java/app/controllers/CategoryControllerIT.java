package app.controllers;

import app.enums.CategoryType;
import app.services.interfaces.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

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
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categoryService.getAllCategories())));

    }

    @Test
    void shouldGetCategoryByCategoryType() throws Exception {
        CategoryType categoryType = CategoryType.ECONOMY;
        mockMvc.perform(get("http://localhost:8080/api/categories/{category_type}", categoryType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categoryService.getCategoryByType(categoryType))));
    }
}
