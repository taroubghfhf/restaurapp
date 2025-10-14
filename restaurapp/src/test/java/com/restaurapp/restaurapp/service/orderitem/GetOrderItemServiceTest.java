package com.restaurapp.restaurapp.service.orderitem;

import com.restaurapp.restaurapp.domain.model.*;
import com.restaurapp.restaurapp.domain.repository.OrderItemRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOrderItemServiceTest {

    @Mock
    private OrderItemRepositoryJpa orderItemRepositoryJpa;

    @InjectMocks
    private GetOrderItemService getOrderItemService;

    @Test
    void execute_WhenOrderItemsExist_ShouldReturnAllOrderItems() {
        // Given
        Category category = new Category(1, "Bebidas");
        Product product = new Product(1, "Coca Cola", category, 5000, 100, true);
        
        Status status = new Status(1, "AVAILABLE");
        Table table = new Table(1, 4, 1, status);
        
        Role role = new Role(1, "WAITER");
        User waiter = new User(1, "Juan", "juan@example.com", "password", role);
        User chef = new User(2, "Maria", "maria@example.com", "password", role);
        
        OrderTicket orderTicket = new OrderTicket(1, LocalDateTime.now(), table, waiter, chef, status);
        
        OrderItem orderItem1 = new OrderItem(1, orderTicket, product, 2, 10000);
        OrderItem orderItem2 = new OrderItem(2, orderTicket, product, 1, 5000);
        List<OrderItem> expectedOrderItems = Arrays.asList(orderItem1, orderItem2);
        
        when(orderItemRepositoryJpa.findAll()).thenReturn(expectedOrderItems);

        // When
        List<OrderItem> result = getOrderItemService.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(1, result.get(1).getQuantity());
        verify(orderItemRepositoryJpa, times(1)).findAll();
    }

    @Test
    void execute_WhenNoOrderItemsExist_ShouldReturnEmptyList() {
        // Given
        when(orderItemRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // When
        List<OrderItem> result = getOrderItemService.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderItemRepositoryJpa, times(1)).findAll();
    }
}

