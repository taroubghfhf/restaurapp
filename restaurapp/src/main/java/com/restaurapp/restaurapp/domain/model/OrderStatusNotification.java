package com.restaurapp.restaurapp.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class OrderStatusNotification {
    private int orderTicketId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;
    
    private String statusName;
    private int statusId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    private int tableId;
    private String tableName;
    private int waiterId;
    private String waiterName;
    private int chefId;
    private String chefName;

    public OrderStatusNotification() {
        this.timestamp = LocalDateTime.now();
    }

    public OrderStatusNotification(int orderTicketId, LocalDateTime orderDate, String statusName, int statusId, 
                                   int tableId, String tableName, int waiterId, String waiterName, 
                                   int chefId, String chefName) {
        this.orderTicketId = orderTicketId;
        this.orderDate = orderDate;
        this.statusName = statusName;
        this.statusId = statusId;
        this.timestamp = LocalDateTime.now();
        this.tableId = tableId;
        this.tableName = tableName;
        this.waiterId = waiterId;
        this.waiterName = waiterName;
        this.chefId = chefId;
        this.chefName = chefName;
    }

    // Getters y Setters
    public int getOrderTicketId() {
        return orderTicketId;
    }

    public void setOrderTicketId(int orderTicketId) {
        this.orderTicketId = orderTicketId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(int waiterId) {
        this.waiterId = waiterId;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public int getChefId() {
        return chefId;
    }

    public void setChefId(int chefId) {
        this.chefId = chefId;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }
}

