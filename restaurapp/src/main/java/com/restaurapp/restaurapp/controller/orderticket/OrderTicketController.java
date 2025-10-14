package com.restaurapp.restaurapp.controller.orderticket;

import com.restaurapp.restaurapp.domain.model.OrderItem;
import com.restaurapp.restaurapp.domain.model.OrderTicket;
import com.restaurapp.restaurapp.service.orderticket.CreateOrderTicketService;
import com.restaurapp.restaurapp.service.orderticket.DeleteOrderTicketService;
import com.restaurapp.restaurapp.service.orderticket.GetOrderItemsService;
import com.restaurapp.restaurapp.service.orderticket.GetOrederTicketService;
import com.restaurapp.restaurapp.service.orderticket.UpdateOrderTicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/order-ticket")
public class OrderTicketController {
    private final CreateOrderTicketService createOrderTicketService;
    private final GetOrederTicketService getOrederTicketService;
    private final UpdateOrderTicketService updateOrderTicketService;
    private final DeleteOrderTicketService deleteOrderTicketService;
    private final GetOrderItemsService getOrderItemsService;

    public OrderTicketController(CreateOrderTicketService createOrderTicketService,
                                GetOrederTicketService getOrederTicketService,
                                UpdateOrderTicketService updateOrderTicketService,
                                DeleteOrderTicketService deleteOrderTicketService,
                                GetOrderItemsService getOrderItemsService) {
        this.createOrderTicketService = createOrderTicketService;
        this.getOrederTicketService = getOrederTicketService;
        this.updateOrderTicketService = updateOrderTicketService;
        this.deleteOrderTicketService = deleteOrderTicketService;
        this.getOrderItemsService = getOrderItemsService;
    }

    @PostMapping
    public ResponseEntity<OrderTicket> create(@RequestBody OrderTicket orderTicket) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrderTicketService.execute(orderTicket));
    }

    @GetMapping
    public ResponseEntity<List<OrderTicket>> getOrderTickets() {
        return ResponseEntity.status(HttpStatus.OK).body(getOrederTicketService.execute());
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<OrderTicket>> getOrderTicketsByStatus(@PathVariable int statusId) {
        return ResponseEntity.status(HttpStatus.OK).body(getOrederTicketService.executeByStatus(statusId));
    }

    @GetMapping("/waiter/{waiterId}")
    public ResponseEntity<List<OrderTicket>> getOrderTicketsByWaiter(@PathVariable int waiterId) {
        return ResponseEntity.status(HttpStatus.OK).body(getOrederTicketService.executeByWaiter(waiterId));
    }

    @GetMapping("/chef/{chefId}")
    public ResponseEntity<List<OrderTicket>> getOrderTicketsByChef(@PathVariable int chefId) {
        return ResponseEntity.status(HttpStatus.OK).body(getOrederTicketService.executeByChef(chefId));
    }

    @GetMapping("/{orderTicketId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable int orderTicketId) {
        return ResponseEntity.status(HttpStatus.OK).body(getOrderItemsService.execute(orderTicketId));
    }

    @PutMapping
    public ResponseEntity<OrderTicket> update(@RequestBody OrderTicket orderTicket) {
        updateOrderTicketService.execute(orderTicket);
        return ResponseEntity.status(HttpStatus.OK).body(orderTicket);
    }

    @PatchMapping("/{orderTicketId}/status/{statusId}")
    public ResponseEntity<OrderTicket> updateStatus(@PathVariable int orderTicketId, @PathVariable int statusId) {
        OrderTicket updatedOrderTicket = updateOrderTicketService.updateStatus(orderTicketId, statusId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrderTicket);
    }

    @DeleteMapping
    public ResponseEntity<OrderTicket> deleteOrderTicket(@RequestBody OrderTicket orderTicket) {
        deleteOrderTicketService.execute(orderTicket);
        return ResponseEntity.status(HttpStatus.OK).body(orderTicket);
    }
}

