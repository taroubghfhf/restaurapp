package com.restaurapp.restaurapp.service.orderticket;

import com.restaurapp.restaurapp.domain.model.OrderTicket;
import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.model.Table;
import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.repository.OrderTicketRepositoryJpa;
import com.restaurapp.restaurapp.domain.repository.StatusRepositoryJpa;
import com.restaurapp.restaurapp.domain.repository.TableRepositoryJpa;
import com.restaurapp.restaurapp.domain.repository.UserRepositoryJpa;
import com.restaurapp.restaurapp.service.notification.OrderNotificationService;
import jakarta.transaction.Transactional;

public class CreateOrderTicketService {

    private final OrderTicketRepositoryJpa orderTicketRepositoryJpa;
    private final TableRepositoryJpa tableRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;
    private final StatusRepositoryJpa statusRepositoryJpa;
    private final OrderNotificationService notificationService;

    public CreateOrderTicketService(OrderTicketRepositoryJpa orderTicketRepositoryJpa,
                                   TableRepositoryJpa tableRepositoryJpa,
                                   UserRepositoryJpa userRepositoryJpa,
                                   StatusRepositoryJpa statusRepositoryJpa,
                                   OrderNotificationService notificationService) {
        this.orderTicketRepositoryJpa = orderTicketRepositoryJpa;
        this.tableRepositoryJpa = tableRepositoryJpa;
        this.userRepositoryJpa = userRepositoryJpa;
        this.statusRepositoryJpa = statusRepositoryJpa;
        this.notificationService = notificationService;
    }

    @Transactional
    public OrderTicket execute(OrderTicket orderTicket) {
        // Resolver Table
        if (orderTicket.getTable() != null && orderTicket.getTable().getTableId() > 0) {
            Table table = tableRepositoryJpa.findById((long) orderTicket.getTable().getTableId())
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada con ID: " + orderTicket.getTable().getTableId()));
            orderTicket.setTable(table);
        }
        
        // Resolver Waiter
        if (orderTicket.getWaiter() != null && orderTicket.getWaiter().getUserId() > 0) {
            User waiter = userRepositoryJpa.findById((long) orderTicket.getWaiter().getUserId())
                .orElseThrow(() -> new RuntimeException("Mesero no encontrado con ID: " + orderTicket.getWaiter().getUserId()));
            orderTicket.setWaiter(waiter);
        }
        
        // Resolver Chef
        if (orderTicket.getChef() != null && orderTicket.getChef().getUserId() > 0) {
            User chef = userRepositoryJpa.findById((long) orderTicket.getChef().getUserId())
                .orElseThrow(() -> new RuntimeException("Cocinero no encontrado con ID: " + orderTicket.getChef().getUserId()));
            orderTicket.setChef(chef);
        }
        
        // Resolver Status
        if (orderTicket.getStatus() != null && orderTicket.getStatus().getStatusId() > 0) {
            Status status = statusRepositoryJpa.findById((long) orderTicket.getStatus().getStatusId())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + orderTicket.getStatus().getStatusId()));
            orderTicket.setStatus(status);
        }
        
        OrderTicket savedOrderTicket = orderTicketRepositoryJpa.save(orderTicket);

        notificationService.notifyOrderCreated(orderTicket);
        
        return savedOrderTicket;
    }
}
