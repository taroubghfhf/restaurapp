package com.restaurapp.restaurapp.service.notification;

import com.restaurapp.restaurapp.domain.model.OrderStatusNotification;
import com.restaurapp.restaurapp.domain.model.OrderTicket;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderNotificationService {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    public OrderNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    /**
     * Envía una notificación de cambio de estado a todos los clientes suscritos
     * @param orderTicket La orden que ha cambiado de estado
     */
    public void notifyOrderStatusChange(OrderTicket orderTicket) {
        OrderStatusNotification notification = buildNotification(orderTicket);
        
        messagingTemplate.convertAndSend("/topic/orders", notification);
        
        messagingTemplate.convertAndSend("/topic/orders/" + orderTicket.getOrderTicketId(), notification);
        
        if (orderTicket.getTable() != null) {
            messagingTemplate.convertAndSend("/topic/tables/" + orderTicket.getTable().getTableId(), notification);
        }
    }
    
    /**
     * Envía una notificación cuando se crea una nueva orden
     * @param orderTicket La orden recién creada
     */
    public void notifyOrderCreated(OrderTicket orderTicket) {
        OrderStatusNotification notification = buildNotification(orderTicket);
        
        // Enviar a todos los clientes suscritos al topic general de órdenes
        messagingTemplate.convertAndSend("/topic/orders", notification);
        
        // Enviar a un topic específico para nuevas órdenes (útil para cocina)
        messagingTemplate.convertAndSend("/topic/orders/new", notification);
        
        // Enviar a un topic específico de la orden
        messagingTemplate.convertAndSend("/topic/orders/" + orderTicket.getOrderTicketId(), notification);
        
        // Enviar a un topic específico de la mesa
        if (orderTicket.getTable() != null) {
            messagingTemplate.convertAndSend("/topic/tables/" + orderTicket.getTable().getTableId(), notification);
        }
    }
    
    /**
     * Construye el objeto de notificación a partir de una orden
     */
    private OrderStatusNotification buildNotification(OrderTicket orderTicket) {
        // Fecha de la orden
        LocalDateTime orderDate = orderTicket.getDate();
        
        // Estado
        String statusName = orderTicket.getStatus() != null ? 
                           orderTicket.getStatus().getName() : "Sin estado";
        int statusId = orderTicket.getStatus() != null ? 
                      orderTicket.getStatus().getStatusId() : 0;
        
        // Mesa
        int tableId = orderTicket.getTable() != null ? 
                     orderTicket.getTable().getTableId() : 0;
        String tableName = orderTicket.getTable() != null ? 
                          "Mesa " + orderTicket.getTable().getTableId() : "Sin mesa";
        
        // Mesero
        int waiterId = orderTicket.getWaiter() != null ? 
                      orderTicket.getWaiter().getUserId() : 0;
        String waiterName = orderTicket.getWaiter() != null ? 
                           orderTicket.getWaiter().getName() : "Sin mesero";
        
        // Chef
        int chefId = orderTicket.getChef() != null ? 
                    orderTicket.getChef().getUserId() : 0;
        String chefName = orderTicket.getChef() != null ? 
                         orderTicket.getChef().getName() : "Sin chef";
        
        return new OrderStatusNotification(
            orderTicket.getOrderTicketId(),
            orderDate,
            statusName,
            statusId,
            tableId,
            tableName,
            waiterId,
            waiterName,
            chefId,
            chefName
        );
    }
}

