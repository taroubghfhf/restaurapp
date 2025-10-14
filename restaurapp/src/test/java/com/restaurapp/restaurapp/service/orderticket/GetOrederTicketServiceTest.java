package com.restaurapp.restaurapp.service.orderticket;

import com.restaurapp.restaurapp.domain.model.*;
import com.restaurapp.restaurapp.domain.repository.OrderTicketRepositoryJpa;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOrederTicketServiceTest {

    @Mock
    private OrderTicketRepositoryJpa orderTicketRepositoryJpa;

    @InjectMocks
    private GetOrederTicketService getOrederTicketService;

    @Test
    void execute_WhenOrderTicketsExist_ShouldReturnAllOrderTickets() {
        // Given
        Status status = new Status(1, "AVAILABLE");
        Table table = new Table(1, 4, 1, status);
        
        Role role = new Role(1, "WAITER");
        User waiter = new User(1, "Juan", "juan@example.com", "password", role);
        User chef = new User(2, "Maria", "maria@example.com", "password", role);
        
        OrderTicket orderTicket1 = new OrderTicket(1, LocalDateTime.now(), table, waiter, chef, status);
        OrderTicket orderTicket2 = new OrderTicket(2, LocalDateTime.now(), table, waiter, chef, status);
        List<OrderTicket> expectedOrderTickets = Arrays.asList(orderTicket1, orderTicket2);
        
        when(orderTicketRepositoryJpa.findAll()).thenReturn(expectedOrderTickets);

        // When
        List<OrderTicket> result = getOrederTicketService.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderTicketRepositoryJpa, times(1)).findAll();
    }

    @Test
    void execute_WhenNoOrderTicketsExist_ShouldReturnEmptyList() {
        // Given
        when(orderTicketRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // When
        List<OrderTicket> result = getOrederTicketService.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderTicketRepositoryJpa, times(1)).findAll();
    }

    @Test
    void executeByStatus_WhenOrderTicketsExistWithGivenStatus_ShouldReturnFilteredOrderTickets() {
        // Given
        Status pendingStatus = new Status(1, "PENDIENTE");
        Table table = new Table(1, 4, 1, pendingStatus);
        
        Role role = new Role(1, "WAITER");
        User waiter = new User(1, "Juan", "juan@example.com", "password", role);
        User chef = new User(2, "Maria", "maria@example.com", "password", role);
        
        OrderTicket orderTicket1 = new OrderTicket(1, LocalDateTime.now(), table, waiter, chef, pendingStatus);
        OrderTicket orderTicket2 = new OrderTicket(2, LocalDateTime.now(), table, waiter, chef, pendingStatus);
        List<OrderTicket> expectedOrderTickets = Arrays.asList(orderTicket1, orderTicket2);
        
        when(orderTicketRepositoryJpa.findByStatusStatusId(1)).thenReturn(expectedOrderTickets);

        // When
        List<OrderTicket> result = getOrederTicketService.executeByStatus(1);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("PENDIENTE", result.get(0).getStatus().getName());
        assertEquals("PENDIENTE", result.get(1).getStatus().getName());
        verify(orderTicketRepositoryJpa, times(1)).findByStatusStatusId(1);
    }

    @Test
    void executeByStatus_WhenNoOrderTicketsExistWithGivenStatus_ShouldReturnEmptyList() {
        // Given
        when(orderTicketRepositoryJpa.findByStatusStatusId(1)).thenReturn(Collections.emptyList());

        // When
        List<OrderTicket> result = getOrederTicketService.executeByStatus(1);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderTicketRepositoryJpa, times(1)).findByStatusStatusId(1);
    }

    @Test
    void executeByStatus_WhenDifferentStatusIds_ShouldReturnCorrectOrderTickets() {
        // Given
        Status preparingStatus = new Status(2, "EN_PREPARACION");
        Table table = new Table(1, 4, 1, preparingStatus);
        
        Role role = new Role(1, "WAITER");
        User waiter = new User(1, "Juan", "juan@example.com", "password", role);
        User chef = new User(2, "Maria", "maria@example.com", "password", role);
        
        OrderTicket orderTicket = new OrderTicket(1, LocalDateTime.now(), table, waiter, chef, preparingStatus);
        List<OrderTicket> expectedOrderTickets = Arrays.asList(orderTicket);
        
        when(orderTicketRepositoryJpa.findByStatusStatusId(2)).thenReturn(expectedOrderTickets);

        // When
        List<OrderTicket> result = getOrederTicketService.executeByStatus(2);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EN_PREPARACION", result.get(0).getStatus().getName());
        verify(orderTicketRepositoryJpa, times(1)).findByStatusStatusId(2);
    }

    @Test
    void executeByWaiter_WhenOrderTicketsExistForWaiter_ShouldReturnWaiterOrderTickets() {
        // Given
        Status status = new Status(1, "PENDIENTE");
        Table table = new Table(1, 4, 1, status);
        
        Role waiterRole = new Role(1, "MESERO");
        Role chefRole = new Role(2, "COCINERO");
        User waiter = new User(1, "Juan Mesero", "juan@example.com", "password", waiterRole);
        User chef = new User(2, "Maria Cocinero", "maria@example.com", "password", chefRole);
        
        OrderTicket orderTicket1 = new OrderTicket(1, LocalDateTime.now(), table, waiter, chef, status);
        OrderTicket orderTicket2 = new OrderTicket(2, LocalDateTime.now(), table, waiter, chef, status);
        List<OrderTicket> expectedOrderTickets = Arrays.asList(orderTicket1, orderTicket2);
        
        when(orderTicketRepositoryJpa.findByWaiterUserId(1)).thenReturn(expectedOrderTickets);

        // When
        List<OrderTicket> result = getOrederTicketService.executeByWaiter(1);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getWaiter().getUserId());
        assertEquals("Juan Mesero", result.get(0).getWaiter().getName());
        assertEquals(1, result.get(1).getWaiter().getUserId());
        verify(orderTicketRepositoryJpa, times(1)).findByWaiterUserId(1);
    }

    @Test
    void executeByWaiter_WhenNoOrderTicketsExistForWaiter_ShouldReturnEmptyList() {
        // Given
        when(orderTicketRepositoryJpa.findByWaiterUserId(1)).thenReturn(Collections.emptyList());

        // When
        List<OrderTicket> result = getOrederTicketService.executeByWaiter(1);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderTicketRepositoryJpa, times(1)).findByWaiterUserId(1);
    }

    @Test
    void executeByChef_WhenOrderTicketsExistForChef_ShouldReturnChefOrderTickets() {
        // Given
        Status status = new Status(2, "EN_PREPARACION");
        Table table = new Table(1, 4, 1, status);
        
        Role waiterRole = new Role(1, "MESERO");
        Role chefRole = new Role(2, "COCINERO");
        User waiter = new User(1, "Juan Mesero", "juan@example.com", "password", waiterRole);
        User chef = new User(2, "Carlos Cocinero", "carlos@example.com", "password", chefRole);
        
        OrderTicket orderTicket1 = new OrderTicket(1, LocalDateTime.now(), table, waiter, chef, status);
        OrderTicket orderTicket2 = new OrderTicket(2, LocalDateTime.now(), table, waiter, chef, status);
        List<OrderTicket> expectedOrderTickets = Arrays.asList(orderTicket1, orderTicket2);
        
        when(orderTicketRepositoryJpa.findByChefUserId(2)).thenReturn(expectedOrderTickets);

        // When
        List<OrderTicket> result = getOrederTicketService.executeByChef(2);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getChef().getUserId());
        assertEquals("Carlos Cocinero", result.get(0).getChef().getName());
        assertEquals(2, result.get(1).getChef().getUserId());
        verify(orderTicketRepositoryJpa, times(1)).findByChefUserId(2);
    }

    @Test
    void executeByChef_WhenNoOrderTicketsExistForChef_ShouldReturnEmptyList() {
        // Given
        when(orderTicketRepositoryJpa.findByChefUserId(2)).thenReturn(Collections.emptyList());

        // When
        List<OrderTicket> result = getOrederTicketService.executeByChef(2);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderTicketRepositoryJpa, times(1)).findByChefUserId(2);
    }

    @Test
    void executeByWaiter_WhenMultipleWaiters_ShouldReturnOnlySpecificWaiterOrderTickets() {
        // Given
        Status status = new Status(1, "PENDIENTE");
        Table table = new Table(1, 4, 1, status);
        
        Role waiterRole = new Role(1, "MESERO");
        Role chefRole = new Role(2, "COCINERO");
        User waiter1 = new User(1, "Juan Mesero", "juan@example.com", "password", waiterRole);
        User chef = new User(3, "Carlos Cocinero", "carlos@example.com", "password", chefRole);
        
        OrderTicket orderTicket1 = new OrderTicket(1, LocalDateTime.now(), table, waiter1, chef, status);
        List<OrderTicket> expectedOrderTickets = Arrays.asList(orderTicket1);
        
        when(orderTicketRepositoryJpa.findByWaiterUserId(1)).thenReturn(expectedOrderTickets);

        // When
        List<OrderTicket> result = getOrederTicketService.executeByWaiter(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getWaiter().getUserId());
        verify(orderTicketRepositoryJpa, times(1)).findByWaiterUserId(1);
    }
}

