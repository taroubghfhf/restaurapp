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
import static org.mockito.ArgumentMatchers.anyString;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductServiceTest {

    @Mock
    private ProductRepositoryJpa productRepositoryJpa;

    @InjectMocks
    private CreateProductService createProductService;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1, "Bebidas");
        product = new Product();
        product.setProductId(1);
        product.setName("Coca Cola");
        product.setCategory(category);
        product.setPrice(5000);
        product.setStock(100);
        product.setStatus(true);
    }

    @Test
    void execute_WhenProductDoesNotExist_ShouldCreateProduct() {
        // Given
        when(productRepositoryJpa.findByName(anyString())).thenReturn(null);
        when(productRepositoryJpa.save(any(Product.class))).thenReturn(product);

        // When
        createProductService.execute(product);

        // Then
        verify(productRepositoryJpa, times(1)).findByName("Coca Cola");
        verify(productRepositoryJpa, times(1)).save(product);
    }

    @Test
    void execute_WhenProductExists_ShouldThrowException() {
        // Given
        when(productRepositoryJpa.findByName(anyString())).thenReturn(product);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createProductService.execute(product);
        });

        assertEquals("El producto ya existe", exception.getMessage());
        verify(productRepositoryJpa, times(1)).findByName("Coca Cola");
        verify(productRepositoryJpa, never()).save(any(Product.class));
    }
}

