package com.restaurapp.restaurapp.domain.repository;

import com.restaurapp.restaurapp.domain.model.OrderTicket;
import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTicketRepositoryJpa extends JpaRepository<OrderTicket,Long > {
    List<OrderTicket> findByStatus(Status status);
    List<OrderTicket> findByStatusStatusId(int statusId);
    List<OrderTicket> findByWaiter(User waiter);
    List<OrderTicket> findByWaiterUserId(int waiterId);
    List<OrderTicket> findByChef(User chef);
    List<OrderTicket> findByChefUserId(int chefId);
}
