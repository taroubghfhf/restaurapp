package com.restaurapp.restaurapp.controller.orderitem;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.model.OrderItem;
import com.restaurapp.restaurapp.service.orderitem.CreateOrderItemService;
import com.restaurapp.restaurapp.service.orderitem.DeleteOrderItemService;
import com.restaurapp.restaurapp.service.orderitem.GetOrderItemService;
import com.restaurapp.restaurapp.service.orderitem.UpdateOrderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/order-item")
public class OrderItemController {

    private final CreateOrderItemService createOrderItemService;
    private final GetOrderItemService getOrderItemService;
    private final UpdateOrderItemService updateOrderItemService;
    private final DeleteOrderItemService deleteOrderItemService;

    public OrderItemController(CreateOrderItemService createOrderItemService, GetOrderItemService getOrderItemService,
                               UpdateOrderItemService updateOrderItemService, DeleteOrderItemService deleteOrderItemService) {
        this.createOrderItemService = createOrderItemService;
        this.getOrderItemService = getOrderItemService;
        this.updateOrderItemService = updateOrderItemService;
        this.deleteOrderItemService = deleteOrderItemService;
    }

    @PostMapping
    public ResponseEntity<OrderItem> create(@RequestBody OrderItem orderItem) {
        createOrderItemService.execute(orderItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItem);
    }

    @GetMapping
    public ResponseEntity<List<OrderItem>> getOrderItem() {
        return ResponseEntity.status(HttpStatus.OK).body(getOrderItemService.execute());
    }

    @PutMapping
    public ResponseEntity<OrderItem> updateOrderItem(@RequestBody OrderItem orderItem){
        updateOrderItemService.execute(orderItem);
        return ResponseEntity.status(HttpStatus.OK).body(orderItem);
    }

    @DeleteMapping
    public ResponseEntity<OrderItem> delete(@RequestBody OrderItem orderItem){
        deleteOrderItemService.execute(orderItem);
        return ResponseEntity.status(HttpStatus.OK).body(orderItem);
    }
}
