package com.restaurapp.restaurapp.service.product;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.model.Product;
import com.restaurapp.restaurapp.domain.repository.ProductRepositoryJpa;
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
class DeleteProductServiceTest {

    @Mock
    private ProductRepositoryJpa productRepositoryJpa;

    @InjectMocks
    private DeleteProductService deleteProductService;

    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category(1, "Bebidas");
        product = new Product();
        product.setProductId(1);
        product.setName("Coca Cola");
        product.setCategory(category);
        product.setPrice(5000);
        product.setStock(100);
        product.setStatus(true);
    }

    @Test
    void execute_WhenProductExists_ShouldDeleteProduct() {
        // Given
        doReturn(Optional.of(product)).when(productRepositoryJpa).findById(anyLong());
        doNothing().when(productRepositoryJpa).delete(any(Product.class));

        // When
        deleteProductService.execute(product);

        // Then
        verify(productRepositoryJpa, times(1)).findById(1L);
        verify(productRepositoryJpa, times(1)).delete(product);
    }

    @Test
    void execute_WhenProductDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(productRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteProductService.execute(product);
        });

        assertEquals("El producto no existe", exception.getMessage());
        verify(productRepositoryJpa, times(1)).findById(1L);
        verify(productRepositoryJpa, never()).delete(any(Product.class));
    }
}

