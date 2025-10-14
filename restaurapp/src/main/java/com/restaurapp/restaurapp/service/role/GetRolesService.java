package com.restaurapp.restaurapp.service.role;

import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.repository.RoleRepositoryJpa;

import java.util.List;

public class GetRolesService {

    private final RoleRepositoryJpa roleRepositoryJpa;

    public GetRolesService(RoleRepositoryJpa roleRepositoryJpa) {
        this.roleRepositoryJpa = roleRepositoryJpa;
    }

    public List<Role> execute() {
        return this.roleRepositoryJpa.findAll();
    }
}
