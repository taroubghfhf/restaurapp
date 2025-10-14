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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOrderItemServiceTest {

    @Mock
    private OrderItemRepositoryJpa orderItemRepositoryJpa;

    @InjectMocks
    private UpdateOrderItemService updateOrderItemService;

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
        orderItem.setQuantity(3);
        orderItem.setSubtotal(15000);
    }

    @Test
    void execute_WhenOrderItemExists_ShouldUpdateOrderItem() {
        // Given
        doReturn(Optional.of(orderItem)).when(orderItemRepositoryJpa).findById(anyLong());
        when(orderItemRepositoryJpa.save(any(OrderItem.class))).thenReturn(orderItem);

        // When
        updateOrderItemService.execute(orderItem);

        // Then
        verify(orderItemRepositoryJpa, times(1)).findById(1L);
        verify(orderItemRepositoryJpa, times(1)).save(orderItem);
    }

    @Test
    void execute_WhenOrderItemDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(orderItemRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateOrderItemService.execute(orderItem);
        });

        assertEquals("la comanda detalle no existe", exception.getMessage());
        verify(orderItemRepositoryJpa, times(1)).findById(1L);
        verify(orderItemRepositoryJpa, never()).save(any(OrderItem.class));
    }
}

