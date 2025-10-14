package com.restaurapp.restaurapp.service.category;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.repository.CategoryRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class DeleteCategoryService {
    private final CategoryRepositoryJpa categoryRepositoryJpa;

    public DeleteCategoryService(CategoryRepositoryJpa categoryRepositoryJpa) {
        this.categoryRepositoryJpa = categoryRepositoryJpa;
    }

    @Transactional
    public void execute(Category category){
        if (this.categoryRepositoryJpa.findById(category.getCategoryId()*1l).isEmpty()){
            throw new RuntimeException("La categoría no existe");
        }
        this.categoryRepositoryJpa.delete(category);
    }
}
