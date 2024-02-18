package app.controllers.api.rest;

import app.entities.Category;
import app.enums.CategoryType;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "Category API")
@Tag(name = "Category API", description = "API для операций с категорией сиденья (эконом, бизнесс и т.д.)")
@RequestMapping("/api/categories")
public interface CategoryRestApi {

    @GetMapping
    @Operation(summary = "Получение всех сущностей без пагинации")
    ResponseEntity<Page<Category>> getAllCategories();

    @Hidden
    @GetMapping("/{category_type}")
    @Deprecated
    ResponseEntity<Category> getCategoryByType(@PathVariable("category_type") CategoryType categoryType);
}