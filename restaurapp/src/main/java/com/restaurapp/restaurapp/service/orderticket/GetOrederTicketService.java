package com.restaurapp.restaurapp.service.orderticket;

import com.restaurapp.restaurapp.domain.model.OrderTicket;
import com.restaurapp.restaurapp.domain.repository.OrderTicketRepositoryJpa;

import java.util.List;

public class GetOrederTicketService {
    private final OrderTicketRepositoryJpa orderTicketRepositoryJpa;

    public GetOrederTicketService(OrderTicketRepositoryJpa orderTicketRepositoryJpa) {
        this.orderTicketRepositoryJpa = orderTicketRepositoryJpa;
    }
    
    public List<OrderTicket> execute(){
        return orderTicketRepositoryJpa.findAll();
    }
    
    public List<OrderTicket> executeByStatus(int statusId){
        return orderTicketRepositoryJpa.findByStatusStatusId(statusId);
    }
    
    public List<OrderTicket> executeByWaiter(int waiterId){
        return orderTicketRepositoryJpa.findByWaiterUserId(waiterId);
    }
    
    public List<OrderTicket> executeByChef(int chefId){
        return orderTicketRepositoryJpa.findByChefUserId(chefId);
    }
}
