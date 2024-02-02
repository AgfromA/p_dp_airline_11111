package app.controllers.rest;

import app.controllers.api.rest.CategoryRestApi;
import app.entities.Category;
import app.enums.CategoryType;
import app.services.CategoryService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class CategoryRestController implements CategoryRestApi {

    private final CategoryService categoryService;

    @Override
    public ResponseEntity<Page<Category>> getAllCategories() {
        var categories = categoryService.getAllCategories();
        if (categories != null) {
            log.info("getAll: find all Categories");
            return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(categories)));
        } else {
            log.info("getAll: Categories not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Hidden
    @Override
    @Deprecated
    public ResponseEntity<Category> getCategoryByType(CategoryType categoryType) {
        var category = categoryService.getCategoryByType(categoryType);
        if (category != null) {
            log.info("getByCategoryType: get by Category type: {}, id: {} ", categoryType, category.getId());
            return new ResponseEntity<>(category, HttpStatus.OK);
        } else {
            log.info("getByCategoryType: not found by Category type: {}", categoryType);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}