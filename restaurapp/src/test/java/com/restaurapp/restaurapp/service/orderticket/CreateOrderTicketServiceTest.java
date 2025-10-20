package com.restaurapp.restaurapp.service.orderticket;

import com.restaurapp.restaurapp.domain.model.*;
import com.restaurapp.restaurapp.domain.repository.OrderTicketRepositoryJpa;
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
class CreateOrderTicketServiceTest {

    @Mock
    private OrderTicketRepositoryJpa orderTicketRepositoryJpa;

    @Mock
    private com.restaurapp.restaurapp.domain.repository.TableRepositoryJpa tableRepositoryJpa;

    @Mock
    private com.restaurapp.restaurapp.domain.repository.UserRepositoryJpa userRepositoryJpa;

    @Mock
    private com.restaurapp.restaurapp.domain.repository.StatusRepositoryJpa statusRepositoryJpa;

    @Mock
    private com.restaurapp.restaurapp.service.notification.OrderNotificationService notificationService;

    @InjectMocks
    private CreateOrderTicketService createOrderTicketService;

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
    void execute_ShouldCreateOrderTicket() {
        // Given
        when(tableRepositoryJpa.findById(1L)).thenReturn(Optional.of(orderTicket.getTable()));
        when(userRepositoryJpa.findById(1L)).thenReturn(Optional.of(orderTicket.getWaiter()));
        when(userRepositoryJpa.findById(2L)).thenReturn(Optional.of(orderTicket.getChef()));
        when(statusRepositoryJpa.findById(1L)).thenReturn(Optional.of(orderTicket.getStatus()));
        when(orderTicketRepositoryJpa.save(any(OrderTicket.class))).thenReturn(orderTicket);
        doNothing().when(notificationService).notifyOrderCreated(any(OrderTicket.class));

        // When
        createOrderTicketService.execute(orderTicket);

        // Then
        verify(orderTicketRepositoryJpa, times(1)).save(orderTicket);
        verify(notificationService, times(1)).notifyOrderCreated(any(OrderTicket.class));
    }
}

