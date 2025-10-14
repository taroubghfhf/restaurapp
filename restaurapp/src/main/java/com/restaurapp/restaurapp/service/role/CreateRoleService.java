package com.restaurapp.restaurapp.service.role;

import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.repository.RoleRepositoryJpa;
import jakarta.transaction.Transactional;

public class CreateRoleService {

    private final RoleRepositoryJpa roleRepositoryJpa;

    public CreateRoleService(RoleRepositoryJpa roleRepositoryJpa) {
        this.roleRepositoryJpa = roleRepositoryJpa;
    }

    @Transactional
    public void execute(Role role) {
        if (roleRepositoryJpa.findByName(role.getName()) != null) {
            throw new RuntimeException("El role ya existe");
        }
        roleRepositoryJpa.save(role);

    }
}
