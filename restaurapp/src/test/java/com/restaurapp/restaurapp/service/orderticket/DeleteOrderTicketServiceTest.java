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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteOrderTicketServiceTest {

    @Mock
    private OrderTicketRepositoryJpa orderTicketRepositoryJpa;

    @InjectMocks
    private DeleteOrderTicketService deleteOrderTicketService;

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
    void execute_WhenOrderTicketExists_ShouldDeleteOrderTicket() {
        // Given
        doReturn(Optional.of(orderTicket)).when(orderTicketRepositoryJpa).findById(anyLong());
        doNothing().when(orderTicketRepositoryJpa).delete(any(OrderTicket.class));

        // When
        deleteOrderTicketService.execute(orderTicket);

        // Then
        verify(orderTicketRepositoryJpa, times(1)).findById(1L);
        verify(orderTicketRepositoryJpa, times(1)).delete(orderTicket);
    }

    @Test
    void execute_WhenOrderTicketDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(orderTicketRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteOrderTicketService.execute(orderTicket);
        });

        assertEquals("La comanda no existe", exception.getMessage());
        verify(orderTicketRepositoryJpa, times(1)).findById(1L);
        verify(orderTicketRepositoryJpa, never()).delete(any(OrderTicket.class));
    }
}

