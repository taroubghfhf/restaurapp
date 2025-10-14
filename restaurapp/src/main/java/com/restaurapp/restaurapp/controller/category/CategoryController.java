package com.restaurapp.restaurapp.controller.category;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.service.category.CreateCategoryService;
import com.restaurapp.restaurapp.service.category.DeleteCategoryService;
import com.restaurapp.restaurapp.service.category.GetCategoryService;
import com.restaurapp.restaurapp.service.category.UpdateCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {
    private final CreateCategoryService createCategoryService;
    private final GetCategoryService getCategoryService;
    private final UpdateCategoryService updateCategoryService;
    private final DeleteCategoryService deleteCategoryService;

    public CategoryController(CreateCategoryService createCategoryService, GetCategoryService getCategoryService,
                              UpdateCategoryService updateCategoryService, DeleteCategoryService deleteCategoryService)
    {
        this.createCategoryService = createCategoryService;
        this.getCategoryService = getCategoryService;
        this.updateCategoryService = updateCategoryService;
        this.deleteCategoryService = deleteCategoryService;
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category){
        return ResponseEntity.status(HttpStatus.CREATED).body(createCategoryService.execute(category));
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(){
        return ResponseEntity.status(HttpStatus.OK).body(getCategoryService.execute());
    }

    @PutMapping
    public ResponseEntity<Category> update(@RequestBody Category category){
        updateCategoryService.execute(category);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @DeleteMapping
    public ResponseEntity<Category> deleteCategory(@RequestBody Category category){
        deleteCategoryService.execute(category);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }
}
