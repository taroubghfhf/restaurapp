package com.restaurapp.restaurapp.domain.repository;

import com.restaurapp.restaurapp.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryJpa extends JpaRepository<User, Long> {
    User findByName(String name);
    User findByEmail(String email);
}
