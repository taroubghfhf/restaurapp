package com.restaurapp.restaurapp.service.role;

import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.repository.RoleRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class DeleteRoleService {
    private final RoleRepositoryJpa roleRepositoryJpa;

    public DeleteRoleService(RoleRepositoryJpa roleRepositoryJpa) {
        this.roleRepositoryJpa = roleRepositoryJpa;
    }

    @Transactional
    public void execute(Role role) {
        if (this.roleRepositoryJpa.findById(role.getRoleId() * 1L).isEmpty()) {
            throw new RuntimeException("El rol no existe");
        }
        this.roleRepositoryJpa.delete(role);
    }
}
