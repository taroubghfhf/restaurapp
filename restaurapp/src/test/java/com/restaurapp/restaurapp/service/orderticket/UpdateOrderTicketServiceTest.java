package com.restaurapp.restaurapp.service.orderticket;

import com.restaurapp.restaurapp.domain.model.*;
import com.restaurapp.restaurapp.domain.repository.OrderTicketRepositoryJpa;
import com.restaurapp.restaurapp.domain.repository.StatusRepositoryJpa;
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
class UpdateOrderTicketServiceTest {

    @Mock
    private OrderTicketRepositoryJpa orderTicketRepositoryJpa;

    @Mock
    private StatusRepositoryJpa statusRepositoryJpa;

    @Mock
    private com.restaurapp.restaurapp.service.notification.OrderNotificationService notificationService;

    @InjectMocks
    private UpdateOrderTicketService updateOrderTicketService;

    private OrderTicket orderTicket;

    @BeforeEach
    void setUp() {
        Status status = new Status(1, "AVAILABLE");
        Table table = new Table(1, 4, 1, status);
        
        Role role = new Role(1, "WAITER");
        User waiter = new User(1, "Juan", "juan@example.com", "password", role);
        User chef = new User(2, "Maria", "maria@example.com", "password", role);
        
        orderTicket = new OrderTicket();
        orderTicket.setOrderTicketId(1);
        orderTicket.setDate(LocalDateTime.now());
        orderTicket.setTable(table);
        orderTicket.setWaiter(waiter);
        orderTicket.setChef(chef);
        orderTicket.setStatus(status);
    }

    @Test
    void execute_WhenOrderTicketExists_ShouldUpdateOrderTicket() {
        // Given
        doReturn(Optional.of(orderTicket)).when(orderTicketRepositoryJpa).findById(anyLong());
        when(orderTicketRepositoryJpa.save(any(OrderTicket.class))).thenReturn(orderTicket);
        doNothing().when(notificationService).notifyOrderStatusChange(any(OrderTicket.class));

        // When
        updateOrderTicketService.execute(orderTicket);

        // Then
        verify(orderTicketRepositoryJpa, times(1)).findById(1L);
        verify(orderTicketRepositoryJpa, times(1)).save(orderTicket);
        verify(notificationService, times(1)).notifyOrderStatusChange(any(OrderTicket.class));
    }

    @Test
    void execute_WhenOrderTicketDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(orderTicketRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateOrderTicketService.execute(orderTicket);
        });

        assertEquals("la comanda no existe", exception.getMessage());
        verify(orderTicketRepositoryJpa, times(1)).findById(1L);
        verify(orderTicketRepositoryJpa, never()).save(any(OrderTicket.class));
    }

    @Test
    void updateStatus_WhenOrderTicketAndStatusExist_ShouldUpdateStatus() {
        // Given
        Status newStatus = new Status(2, "EN_PREPARACION");
        OrderTicket updatedOrderTicket = new OrderTicket();
        updatedOrderTicket.setOrderTicketId(1);
        updatedOrderTicket.setDate(orderTicket.getDate());
        updatedOrderTicket.setTable(orderTicket.getTable());
        updatedOrderTicket.setWaiter(orderTicket.getWaiter());
        updatedOrderTicket.setChef(orderTicket.getChef());
        updatedOrderTicket.setStatus(newStatus);
        
        when(orderTicketRepositoryJpa.findById(1L)).thenReturn(Optional.of(orderTicket));
        when(statusRepositoryJpa.findById(2L)).thenReturn(Optional.of(newStatus));
        when(orderTicketRepositoryJpa.save(any(OrderTicket.class))).thenReturn(updatedOrderTicket);
        doNothing().when(notificationService).notifyOrderStatusChange(any(OrderTicket.class));

        // When
        OrderTicket result = updateOrderTicketService.updateStatus(1, 2);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getStatus().getStatusId());
        assertEquals("EN_PREPARACION", result.getStatus().getName());
        verify(orderTicketRepositoryJpa, times(1)).findById(1L);
        verify(statusRepositoryJpa, times(1)).findById(2L);
        verify(orderTicketRepositoryJpa, times(1)).save(any(OrderTicket.class));
        verify(notificationService, times(1)).notifyOrderStatusChange(any(OrderTicket.class));
    }

    @Test
    void updateStatus_WhenOrderTicketDoesNotExist_ShouldThrowException() {
        // Given
        when(orderTicketRepositoryJpa.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateOrderTicketService.updateStatus(1, 2);
        });

        assertEquals("La orden no existe", exception.getMessage());
        verify(orderTicketRepositoryJpa, times(1)).findById(1L);
        verify(statusRepositoryJpa, never()).findById(anyLong());
        verify(orderTicketRepositoryJpa, never()).save(any(OrderTicket.class));
    }

    @Test
    void updateStatus_WhenStatusDoesNotExist_ShouldThrowException() {
        // Given
        when(orderTicketRepositoryJpa.findById(1L)).thenReturn(Optional.of(orderTicket));
        when(statusRepositoryJpa.findById(2L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateOrderTicketService.updateStatus(1, 2);
        });

        assertEquals("El estado no existe", exception.getMessage());
        verify(orderTicketRepositoryJpa, times(1)).findById(1L);
        verify(statusRepositoryJpa, times(1)).findById(2L);
        verify(orderTicketRepositoryJpa, never()).save(any(OrderTicket.class));
    }

    @Test
    void updateStatus_WhenChangingFromPendingToInPreparation_ShouldUpdateSuccessfully() {
        // Given
        Status pendingStatus = new Status(1, "PENDIENTE");
        Status preparingStatus = new Status(2, "EN_PREPARACION");
        
        orderTicket.setStatus(pendingStatus);
        
        when(orderTicketRepositoryJpa.findById(1L)).thenReturn(Optional.of(orderTicket));
        when(statusRepositoryJpa.findById(2L)).thenReturn(Optional.of(preparingStatus));
        when(orderTicketRepositoryJpa.save(any(OrderTicket.class))).thenAnswer(invocation -> {
            OrderTicket savedTicket = invocation.getArgument(0);
            return savedTicket;
        });
        doNothing().when(notificationService).notifyOrderStatusChange(any(OrderTicket.class));

        // When
        OrderTicket result = updateOrderTicketService.updateStatus(1, 2);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getStatus().getStatusId());
        assertEquals("EN_PREPARACION", result.getStatus().getName());
        verify(orderTicketRepositoryJpa, times(1)).findById(1L);
        verify(statusRepositoryJpa, times(1)).findById(2L);
        verify(orderTicketRepositoryJpa, times(1)).save(any(OrderTicket.class));
        verify(notificationService, times(1)).notifyOrderStatusChange(any(OrderTicket.class));
    }

    @Test
    void updateStatus_WhenChangingToReady_ShouldUpdateSuccessfully() {
        // Given
        Status preparingStatus = new Status(2, "EN_PREPARACION");
        Status readyStatus = new Status(3, "LISTO");
        
        orderTicket.setStatus(preparingStatus);
        
        when(orderTicketRepositoryJpa.findById(1L)).thenReturn(Optional.of(orderTicket));
        when(statusRepositoryJpa.findById(3L)).thenReturn(Optional.of(readyStatus));
        when(orderTicketRepositoryJpa.save(any(OrderTicket.class))).thenAnswer(invocation -> {
            OrderTicket savedTicket = invocation.getArgument(0);
            return savedTicket;
        });
        doNothing().when(notificationService).notifyOrderStatusChange(any(OrderTicket.class));

        // When
        OrderTicket result = updateOrderTicketService.updateStatus(1, 3);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getStatus().getStatusId());
        assertEquals("LISTO", result.getStatus().getName());
        verify(orderTicketRepositoryJpa, times(1)).findById(1L);
        verify(statusRepositoryJpa, times(1)).findById(3L);
        verify(orderTicketRepositoryJpa, times(1)).save(any(OrderTicket.class));
        verify(notificationService, times(1)).notifyOrderStatusChange(any(OrderTicket.class));
    }
}

