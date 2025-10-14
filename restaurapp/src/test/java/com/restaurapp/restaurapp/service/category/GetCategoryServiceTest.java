package com.restaurapp.restaurapp.service.category;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.repository.CategoryRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCategoryServiceTest {

    @Mock
    private CategoryRepositoryJpa categoryRepositoryJpa;

    @InjectMocks
    private GetCategoryService getCategoryService;

    @Test
    void execute_WhenCategoriesExist_ShouldReturnAllCategories() {
        // Given
        Category category1 = new Category(1, "Bebidas");
        Category category2 = new Category(2, "Comidas");
        List<Category> expectedCategories = Arrays.asList(category1, category2);
        
        when(categoryRepositoryJpa.findAll()).thenReturn(expectedCategories);

        // When
        List<Category> result = getCategoryService.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bebidas", result.get(0).getName());
        assertEquals("Comidas", result.get(1).getName());
        verify(categoryRepositoryJpa, times(1)).findAll();
    }

    @Test
    void execute_WhenNoCategoriesExist_ShouldReturnEmptyList() {
        // Given
        when(categoryRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Category> result = getCategoryService.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepositoryJpa, times(1)).findAll();
    }
}

