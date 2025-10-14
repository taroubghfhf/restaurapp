package com.restaurapp.restaurapp.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "statuses")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id", length = 11)
    private int statusId;
    @Column(name = "name", length = 10)
    private String name;

    public Status(){}
    public Status(int statusId, String name) {
        this.statusId = statusId;
        this.name = name;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
