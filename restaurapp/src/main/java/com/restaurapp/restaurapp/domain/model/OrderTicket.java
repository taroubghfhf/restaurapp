package com.restaurapp.restaurapp.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_tickets")
public class OrderTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_ticket_id", length = 11)
    private int orderTicketId;
    
    @Column(name = "date")
    private LocalDateTime date;
    
    @ManyToOne
    @JoinColumn(name = "table_id")
    private com.restaurapp.restaurapp.domain.model.Table table;
    
    @ManyToOne
    @JoinColumn(name = "waiter_id")
    private User waiter;
    
    @ManyToOne
    @JoinColumn(name = "chef_id")
    private User chef;
    
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    public OrderTicket(){}
    public OrderTicket(int orderTicketId, LocalDateTime date, com.restaurapp.restaurapp.domain.model.Table table, User waiter, User chef, Status status) {
        this.orderTicketId = orderTicketId;
        this.date = date;
        this.table = table;
        this.waiter = waiter;
        this.chef = chef;
        this.status = status;
    }

    public int getOrderTicketId() {
        return orderTicketId;
    }

    public void setOrderTicketId(int orderTicketId) {
        this.orderTicketId = orderTicketId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public com.restaurapp.restaurapp.domain.model.Table getTable() {
        return table;
    }

    public void setTable(com.restaurapp.restaurapp.domain.model.Table table) {
        this.table = table;
    }

    public User getWaiter() {
        return waiter;
    }

    public void setWaiter(User waiter) {
        this.waiter = waiter;
    }

    public User getChef() {
        return chef;
    }

    public void setChef(User chef) {
        this.chef = chef;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
