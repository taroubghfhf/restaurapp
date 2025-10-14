package com.restaurapp.restaurapp.domain.repository;

import com.restaurapp.restaurapp.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositoryJpa extends JpaRepository<Product,Long > {
    Product findByName(String name);
}
