package com.restaurapp.restaurapp.service.category;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.repository.CategoryRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryServiceTest {

    @Mock
    private CategoryRepositoryJpa categoryRepositoryJpa;

    @InjectMocks
    private UpdateCategoryService updateCategoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1);
        category.setName("Bebidas Actualizadas");
    }

    @Test
    void execute_WhenCategoryExists_ShouldUpdateCategory() {
        // Given
        doReturn(Optional.of(category)).when(categoryRepositoryJpa).findById(anyLong());
        when(categoryRepositoryJpa.save(any(Category.class))).thenReturn(category);

        // When
        updateCategoryService.execute(category);

        // Then
        verify(categoryRepositoryJpa, times(1)).findById(1L);
        verify(categoryRepositoryJpa, times(1)).save(category);
    }

    @Test
    void execute_WhenCategoryDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(categoryRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateCategoryService.execute(category);
        });

        assertEquals("El nombre de la categoria no existe", exception.getMessage());
        verify(categoryRepositoryJpa, times(1)).findById(1L);
        verify(categoryRepositoryJpa, never()).save(any(Category.class));
    }
}

