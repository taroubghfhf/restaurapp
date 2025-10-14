package com.restaurapp.restaurapp.domain.repository;

import com.restaurapp.restaurapp.domain.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepositoryJpa extends JpaRepository<Phone,Long > {

    Phone findByNumber(String number);
}
