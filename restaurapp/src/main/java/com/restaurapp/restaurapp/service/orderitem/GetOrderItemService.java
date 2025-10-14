package com.restaurapp.restaurapp.service.orderitem;

import com.restaurapp.restaurapp.domain.model.OrderItem;
import com.restaurapp.restaurapp.domain.repository.OrderItemRepositoryJpa;

import java.util.List;

public class GetOrderItemService {
    private final OrderItemRepositoryJpa orderItemRepositoryJpa;

    public GetOrderItemService(OrderItemRepositoryJpa orderItemRepositoryJpa) {
        this.orderItemRepositoryJpa = orderItemRepositoryJpa;
    }
    public List<OrderItem> execute(){
        return orderItemRepositoryJpa.findAll();
    }
}
