package com.restaurapp.restaurapp.domain.repository;

import com.restaurapp.restaurapp.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositoryJpa extends JpaRepository<Role,Long > {

    Role findByName(String name);
}
