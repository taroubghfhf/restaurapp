package com.restaurapp.restaurapp.controller.websocket;

import com.restaurapp.restaurapp.domain.model.OrderStatusNotification;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

/**
 * Controlador WebSocket para manejar las suscripciones y mensajes relacionados con órdenes
 */
@Controller
public class OrderWebSocketController {
    
    /**
     * Maneja la suscripción al topic general de órdenes
     * Los clientes se suscriben a /topic/orders para recibir todas las actualizaciones
     */
    @SubscribeMapping("/orders")
    public String handleOrdersSubscription() {
        return "Suscripción exitosa al canal de órdenes";
    }
    
    /**
     * Maneja la suscripción a una orden específica
     * Los clientes se suscriben a /topic/orders/{orderId}
     */
    @SubscribeMapping("/orders/{orderId}")
    public String handleOrderSubscription(@DestinationVariable int orderId) {
        return "Suscripción exitosa a la orden: " + orderId;
    }
    
    /**
     * Maneja la suscripción a una mesa específica
     * Los clientes se suscriben a /topic/tables/{tableId}
     */
    @SubscribeMapping("/tables/{tableId}")
    public String handleTableSubscription(@DestinationVariable int tableId) {
        return "Suscripción exitosa a la mesa: " + tableId;
    }
    
    /**
     * Endpoint para enviar mensajes desde el cliente (opcional)
     * Los clientes envían mensajes a /app/order/status
     */
    @MessageMapping("/order/status")
    @SendTo("/topic/orders")
    public OrderStatusNotification handleOrderStatusMessage(OrderStatusNotification notification) {
        // Este método permite que los clientes también envíen actualizaciones si es necesario
        return notification;
    }
}

