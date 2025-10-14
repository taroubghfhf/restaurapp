package com.restaurapp.restaurapp.service.orderticket;

import com.restaurapp.restaurapp.domain.model.OrderItem;
import com.restaurapp.restaurapp.domain.repository.OrderItemRepositoryJpa;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class GetOrderItemsService {
    
    private final OrderItemRepositoryJpa orderItemRepositoryJpa;
    
    public GetOrderItemsService(OrderItemRepositoryJpa orderItemRepositoryJpa) {
        this.orderItemRepositoryJpa = orderItemRepositoryJpa;
    }
    
    /**
     * Obtiene todos los items de una orden específica mediante query
     * @param orderTicketId ID de la orden
     * @return Lista de items de la orden
     */
    @Transactional(readOnly = true)
    public List<OrderItem> execute(int orderTicketId) {
        return orderItemRepositoryJpa.findByOrderTicketId(orderTicketId);
    }
}

