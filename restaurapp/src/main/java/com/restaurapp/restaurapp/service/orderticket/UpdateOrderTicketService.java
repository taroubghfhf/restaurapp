package com.restaurapp.restaurapp.service.orderticket;

import com.restaurapp.restaurapp.domain.model.OrderTicket;
import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.repository.OrderTicketRepositoryJpa;
import com.restaurapp.restaurapp.domain.repository.StatusRepositoryJpa;
import com.restaurapp.restaurapp.service.notification.OrderNotificationService;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class UpdateOrderTicketService {
    private final OrderTicketRepositoryJpa orderTicketRepositoryJpa;
    private final StatusRepositoryJpa statusRepositoryJpa;
    private final OrderNotificationService notificationService;

    public UpdateOrderTicketService(OrderTicketRepositoryJpa orderTicketRepositoryJpa, 
                                   StatusRepositoryJpa statusRepositoryJpa,
                                   OrderNotificationService notificationService) {
        this.orderTicketRepositoryJpa = orderTicketRepositoryJpa;
        this.statusRepositoryJpa = statusRepositoryJpa;
        this.notificationService = notificationService;
    }

    @Transactional
    public void execute(OrderTicket orderTicket) {
        if (orderTicketRepositoryJpa.findById(orderTicket.getOrderTicketId() * 1L).isEmpty()) {
            throw new RuntimeException("la comanda no existe");
        }
        OrderTicket savedOrderTicket = orderTicketRepositoryJpa.save(orderTicket);
        
        // Enviar notificación en tiempo real del cambio de estado
        notificationService.notifyOrderStatusChange(savedOrderTicket);
    }
    
    @Transactional
    public OrderTicket updateStatus(int orderTicketId, int statusId) {
        Optional<OrderTicket> orderTicketOptional = orderTicketRepositoryJpa.findById((long) orderTicketId);
        
        if (orderTicketOptional.isEmpty()) {
            throw new RuntimeException("La orden no existe");
        }
        
        Optional<Status> statusOptional = statusRepositoryJpa.findById((long) statusId);
        
        if (statusOptional.isEmpty()) {
            throw new RuntimeException("El estado no existe");
        }
        
        OrderTicket orderTicket = orderTicketOptional.get();
        orderTicket.setStatus(statusOptional.get());
        
        OrderTicket savedOrderTicket = orderTicketRepositoryJpa.save(orderTicket);
        
        
        notificationService.notifyOrderStatusChange(savedOrderTicket);
        
        return savedOrderTicket;
    }
}
