package com.restaurapp.restaurapp.service.category;

import com.restaurapp.restaurapp.domain.exception.ExistRecordException;
import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.repository.CategoryRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryServiceTest {

    @Mock
    private CategoryRepositoryJpa categoryRepositoryJpa;

    @InjectMocks
    private CreateCategoryService createCategoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1);
        category.setName("Bebidas");
    }

    @Test
    void execute_WhenCategoryDoesNotExist_ShouldCreateCategory() {

        when(categoryRepositoryJpa.findByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepositoryJpa.save(any(Category.class))).thenReturn(category);


        Category result = createCategoryService.execute(category);

        assertNotNull(result);
        assertEquals("Bebidas", result.getName());
        verify(categoryRepositoryJpa, times(1)).findByName("Bebidas");
        verify(categoryRepositoryJpa, times(1)).save(category);
    }

    @Test
    void execute_WhenCategoryExists_ShouldThrowException() {

        Category existingCategory = new Category();
        existingCategory.setName("Bebidas");
        when(categoryRepositoryJpa.findByName(anyString())).thenReturn(Optional.of(existingCategory));


        ExistRecordException exception = assertThrows(ExistRecordException.class, () -> {
            createCategoryService.execute(category);
        });

        assertEquals("El nombre de la categoria ya existe", exception.getMessage());
        verify(categoryRepositoryJpa, times(1)).findByName("Bebidas");
        verify(categoryRepositoryJpa, never()).save(any(Category.class));
    }
}

