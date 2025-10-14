package com.restaurapp.restaurapp.domain.repository;

import com.restaurapp.restaurapp.domain.model.UserPhone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPhoneRepositoryJpa extends JpaRepository <UserPhone, Long> {

}
