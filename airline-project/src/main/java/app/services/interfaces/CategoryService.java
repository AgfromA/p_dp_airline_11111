package app.services.interfaces;

import app.entities.Category;
import app.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    void save(Category category);
    List<Category> findAll();
    Category findByCategoryType(CategoryType categoryType);

}
