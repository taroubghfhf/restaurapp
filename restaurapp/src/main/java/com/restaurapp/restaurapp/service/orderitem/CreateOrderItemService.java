package com.restaurapp.restaurapp.service.orderitem;

import com.restaurapp.restaurapp.domain.model.OrderItem;
import com.restaurapp.restaurapp.domain.model.OrderTicket;
import com.restaurapp.restaurapp.domain.repository.OrderItemRepositoryJpa;
import com.restaurapp.restaurapp.domain.repository.OrderTicketRepositoryJpa;
import jakarta.transaction.Transactional;

public class CreateOrderItemService {
    private final OrderItemRepositoryJpa orderItemRepositoryJpa;
    private final OrderTicketRepositoryJpa orderTicketRepositoryJpa;

    public CreateOrderItemService(OrderItemRepositoryJpa orderItemRepositoryJpa,
                                 OrderTicketRepositoryJpa orderTicketRepositoryJpa) {
        this.orderItemRepositoryJpa = orderItemRepositoryJpa;
        this.orderTicketRepositoryJpa = orderTicketRepositoryJpa;
    }

    @Transactional
    public void execute(OrderItem orderItem) {
        // Si el orderTicket viene en el objeto (aunque @JsonIgnore lo impide normalmente),
        // lo usamos. Si no, no hacemos nada especial y JPA manejará la referencia.
        if (orderItem.getOrderTicket() != null && orderItem.getOrderTicket().getOrderTicketId() > 0) {
            OrderTicket orderTicket = orderTicketRepositoryJpa.findById(
                (long) orderItem.getOrderTicket().getOrderTicketId()
            ).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
            orderItem.setOrderTicket(orderTicket);
        }
        orderItemRepositoryJpa.save(orderItem);
    }
}
