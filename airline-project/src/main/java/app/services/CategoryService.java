package app.services;

import app.entities.Category;
import app.enums.CategoryType;
import app.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category getCategoryByType(CategoryType categoryType) {
        return categoryRepository
                .findByCategoryType(categoryType)
                .orElseThrow(() -> new RuntimeException("Передан несуществующий CategoryType"));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}