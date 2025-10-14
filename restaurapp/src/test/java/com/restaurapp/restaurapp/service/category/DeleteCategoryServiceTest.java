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

@ExtendWith(MockitoExtension.class)
class DeleteCategoryServiceTest {

    @Mock
    private CategoryRepositoryJpa categoryRepositoryJpa;

    @InjectMocks
    private DeleteCategoryService deleteCategoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setCategoryId(1);
        category.setName("Bebidas");
    }

    @Test
    void execute_WhenCategoryExists_ShouldDeleteCategory() {

        doReturn(Optional.of(category)).when(categoryRepositoryJpa).findById(anyLong());
        doNothing().when(categoryRepositoryJpa).delete(any(Category.class));


        deleteCategoryService.execute(category);

        verify(categoryRepositoryJpa, times(1)).findById(1L);
        verify(categoryRepositoryJpa, times(1)).delete(category);
    }

    @Test
    void execute_WhenCategoryDoesNotExist_ShouldThrowException() {

        doReturn(Optional.empty()).when(categoryRepositoryJpa).findById(anyLong());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteCategoryService.execute(category);
        });

        assertEquals("La categoría no existe", exception.getMessage());
        verify(categoryRepositoryJpa, times(1)).findById(1L);
        verify(categoryRepositoryJpa, never()).delete(any(Category.class));
    }
}

