package com.restaurapp.restaurapp.service.orderticket;

import com.restaurapp.restaurapp.domain.model.OrderTicket;
import com.restaurapp.restaurapp.domain.repository.OrderTicketRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class DeleteOrderTicketService {
    private final OrderTicketRepositoryJpa orderTicketRepositoryJpa;

    public DeleteOrderTicketService(OrderTicketRepositoryJpa orderTicketRepositoryJpa) {
        this.orderTicketRepositoryJpa = orderTicketRepositoryJpa;
    }

    @Transactional
    public void execute(OrderTicket orderTicket) {
        if (this.orderTicketRepositoryJpa.findById(orderTicket.getOrderTicketId() * 1L).isEmpty()) {
            throw new RuntimeException("La comanda no existe");
        }
        this.orderTicketRepositoryJpa.delete(orderTicket);
    }
}
