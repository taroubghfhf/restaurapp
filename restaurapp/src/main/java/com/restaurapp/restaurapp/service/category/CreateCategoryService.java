package com.restaurapp.restaurapp.service.category;

import com.restaurapp.restaurapp.domain.exception.ExistRecordException;
import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.repository.CategoryRepositoryJpa;
import jakarta.transaction.Transactional;

public class CreateCategoryService {

    private final CategoryRepositoryJpa categoryRepositoryJpa;

    private final static String CATEGORY_EXIST_EXCEPTION = "El nombre de la categoria ya existe";

    public CreateCategoryService(CategoryRepositoryJpa categoryRepositoryJpa) {
        this.categoryRepositoryJpa = categoryRepositoryJpa;
    }

    @Transactional
    public Category execute(Category category) {
        categoryRepositoryJpa.findByName(category.getName()).ifPresent( existing -> {
            throw new ExistRecordException(CATEGORY_EXIST_EXCEPTION);
        });

       return categoryRepositoryJpa.save(category);
    }
}
