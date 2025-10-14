package com.restaurapp.restaurapp.service.category;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.repository.CategoryRepositoryJpa;

import java.util.List;

public class GetCategoryService {

    private final CategoryRepositoryJpa categoryRepositoryJpa;

    public GetCategoryService(CategoryRepositoryJpa categoryRepositoryJpa) {
        this.categoryRepositoryJpa = categoryRepositoryJpa;
    }

    public List<Category> execute(){
        return categoryRepositoryJpa.findAll();
    }
}
