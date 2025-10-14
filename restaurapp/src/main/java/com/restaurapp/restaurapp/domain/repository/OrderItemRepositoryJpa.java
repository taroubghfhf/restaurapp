package com.restaurapp.restaurapp.domain.repository;

import com.restaurapp.restaurapp.domain.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepositoryJpa extends JpaRepository<OrderItem, Long> {
    
    /**
     * Busca todos los items de una orden específica
     * @param orderTicketId ID de la orden
     * @return Lista de items de la orden
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderTicket.orderTicketId = :orderTicketId")
    List<OrderItem> findByOrderTicketId(@Param("orderTicketId") int orderTicketId);
}
