package com.restaurapp.restaurapp.service.category;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.repository.CategoryRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class UpdateCategoryService {
    private final CategoryRepositoryJpa categoryRepositoryJpa;
    public UpdateCategoryService(CategoryRepositoryJpa categoryRepositoryJpa) {
        this.categoryRepositoryJpa = categoryRepositoryJpa;
    }

    @Transactional
    public void execute(Category category) {
        if (categoryRepositoryJpa.findById(category.getCategoryId()*1L).isEmpty()) {
            throw new RuntimeException("El nombre de la categoria no existe");
        }
        categoryRepositoryJpa.save(category);
    }
}
