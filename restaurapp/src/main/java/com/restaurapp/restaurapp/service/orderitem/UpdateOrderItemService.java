package com.restaurapp.restaurapp.service.orderitem;

import com.restaurapp.restaurapp.domain.model.OrderItem;
import com.restaurapp.restaurapp.domain.repository.OrderItemRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class UpdateOrderItemService {
    private final OrderItemRepositoryJpa orderItemRepositoryJpa;

    public UpdateOrderItemService(OrderItemRepositoryJpa orderItemRepositoryJpa) {
        this.orderItemRepositoryJpa = orderItemRepositoryJpa;
    }

    @Transactional
    public void execute(OrderItem orderItem) {
        if (orderItemRepositoryJpa.findById(orderItem.getOrderItemId() * 1L).isEmpty()) {
            throw new RuntimeException("la comanda detalle no existe");
        }
        orderItemRepositoryJpa.save(orderItem);
    }
}
