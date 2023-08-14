package app.services.interfaces;

import app.entities.Category;
import app.enums.CategoryType;

import java.util.List;

public interface CategoryService {
    void saveCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryByType(CategoryType categoryType);

}
