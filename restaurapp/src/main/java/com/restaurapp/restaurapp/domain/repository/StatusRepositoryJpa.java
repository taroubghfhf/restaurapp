package com.restaurapp.restaurapp.domain.repository;

import com.restaurapp.restaurapp.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepositoryJpa extends JpaRepository<Status, Long> {

    Status findByName(String name);
}
