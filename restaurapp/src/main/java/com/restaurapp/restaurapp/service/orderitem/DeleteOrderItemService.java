package com.restaurapp.restaurapp.service.orderitem;

import com.restaurapp.restaurapp.domain.model.OrderItem;
import com.restaurapp.restaurapp.domain.repository.OrderItemRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class DeleteOrderItemService {
    private final OrderItemRepositoryJpa orderItemRepositoryJpa;

    public DeleteOrderItemService(OrderItemRepositoryJpa orderItemRepositoryJpa) {
        this.orderItemRepositoryJpa = orderItemRepositoryJpa;
    }

    @Transactional
    public void execute(OrderItem orderItem) {
        if (this.orderItemRepositoryJpa.findById(orderItem.getOrderItemId() * 1L).isEmpty()) {
            throw new RuntimeException("El detalle de comanda no existe");
        }
        this.orderItemRepositoryJpa.delete(orderItem);
    }
}
