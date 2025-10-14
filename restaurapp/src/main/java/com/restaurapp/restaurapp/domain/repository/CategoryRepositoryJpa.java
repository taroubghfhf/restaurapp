package com.restaurapp.restaurapp.domain.repository;

import com.restaurapp.restaurapp.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepositoryJpa extends JpaRepository<Category,Long > {

    Optional<Category> findByName(String name);
}
