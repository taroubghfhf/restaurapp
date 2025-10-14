package com.restaurapp.restaurapp.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_id", length = 11)
    private int phoneId;
    @Column(name = "number", length = 15)
    private String number;

    public Phone(){}
    public Phone(int phoneId, String number) {
        this.phoneId = phoneId;
        this.number = number;
    }

    public int getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(int phoneId) {
        this.phoneId = phoneId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
