package com.restaurapp.restaurapp.domain.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_phones")
public class UserPhone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_phone_id", length = 11)
    private int userPhoneId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "phone_id")
    private Phone phone;

    public UserPhone(){}
    public UserPhone(int userPhoneId, User user, Phone phone) {
        this.userPhoneId = userPhoneId;
        this.user = user;
        this.phone = phone;
    }

    public int getUserPhoneId() {
        return userPhoneId;
    }

    public void setUserPhoneId(int userPhoneId) {
        this.userPhoneId = userPhoneId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
