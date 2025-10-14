package com.restaurapp.restaurapp.service.orderitem;

import com.restaurapp.restaurapp.domain.model.*;
import com.restaurapp.restaurapp.domain.repository.OrderItemRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderItemServiceTest {

    @Mock
    private OrderItemRepositoryJpa orderItemRepositoryJpa;

    @InjectMocks
    private CreateOrderItemService createOrderItemService;

    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        Category category = new Category(1, "Bebidas");
        Product product = new Product(1, "Coca Cola", category, 5000, 100, true);
        
        Status status = new Status(1, "AVAILABLE");
        Table table = new Table(1, 4, 1, status);
        
        Role role = new Role(1, "WAITER");
        User waiter = new User(1, "Juan", "juan@example.com", "password", role);
        User chef = new User(2, "Maria", "maria@example.com", "password", role);
        
        OrderTicket orderTicket = new OrderTicket(1, LocalDateTime.now(), table, waiter, chef, status);
        
        orderItem = new OrderItem();
        orderItem.setOrderItemId(1);
        orderItem.setOrderTicket(orderTicket);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setSubtotal(10000);
    }

    @Test
    void execute_ShouldCreateOrderItem() {
        // Given
        when(orderItemRepositoryJpa.save(any(OrderItem.class))).thenReturn(orderItem);

        // When
        createOrderItemService.execute(orderItem);

        // Then
        verify(orderItemRepositoryJpa, times(1)).save(orderItem);
    }
}

