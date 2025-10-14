package com.restaurapp.restaurapp.domain.model;

import jakarta.persistence.*;

@Entity
        @jakarta.persistence.Table(name="tables")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="table_id", length = 11)
    private int tableId ;
    @Column(name="capacity", length = 11)
    private int capacity;
    @Column(name="location", length = 50)
    private int location;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    public Table(){}
    public Table(int tableId, int capacity, int location, Status status){
        this.tableId = tableId;
        this.capacity = capacity;
        this.location = location;
        this.status = status;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
