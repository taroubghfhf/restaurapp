package com.restaurapp.restaurapp.domain.repository;


import com.restaurapp.restaurapp.domain.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepositoryJpa extends JpaRepository<Table, Long > {
}
