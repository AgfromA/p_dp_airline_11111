package app.controllers.api.rest;

import app.entities.Category;
import app.enums.CategoryType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "Category REST")
@Tag(name = "Category REST", description = "API для операций с категорией сиденья (эконом, бизнесс и т.д.)")
@RequestMapping("/api/categories")
public interface CategoryRestApi {

    @GetMapping
    @ApiOperation(value = "Get list of all Categories")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Categories found"),
            @ApiResponse(code = 404, message = "Categories not found")
    })
    ResponseEntity<List<Category>> getAllCategories();

    @ApiIgnore
    @GetMapping("/{category_type}")
    @ApiOperation(value = "Get category by CategoryType")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "category found"),
            @ApiResponse(code = 404, message = "category not found")
    })
    @Deprecated
    ResponseEntity<Category> getCategoryByType(
            @ApiParam(
                    name = "category_type",
                    value = "CategoryType model"
            )
            @PathVariable("category_type") CategoryType categoryType);
}