package com.restaurapp.restaurapp.service.product;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.model.Product;
import com.restaurapp.restaurapp.domain.repository.ProductRepositoryJpa;
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
class GetProductServiceTest {

    @Mock
    private ProductRepositoryJpa productRepositoryJpa;

    @InjectMocks
    private GetProductService getProductService;

    @Test
    void execute_WhenProductsExist_ShouldReturnAllProducts() {
        // Given
        Category category = new Category(1, "Bebidas");
        Product product1 = new Product(1, "Coca Cola", category, 5000, 100, true);
        Product product2 = new Product(2, "Pepsi", category, 4500, 80, true);
        List<Product> expectedProducts = Arrays.asList(product1, product2);
        
        when(productRepositoryJpa.findAll()).thenReturn(expectedProducts);

        // When
        List<Product> result = getProductService.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Coca Cola", result.get(0).getName());
        assertEquals("Pepsi", result.get(1).getName());
        verify(productRepositoryJpa, times(1)).findAll();
    }

    @Test
    void execute_WhenNoProductsExist_ShouldReturnEmptyList() {
        // Given
        when(productRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Product> result = getProductService.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepositoryJpa, times(1)).findAll();
    }
}

