package com.restaurapp.restaurapp.controller.role;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.service.category.CreateCategoryService;
import com.restaurapp.restaurapp.service.role.CreateRoleService;
import com.restaurapp.restaurapp.service.role.DeleteRoleService;
import com.restaurapp.restaurapp.service.role.GetRolesService;
import com.restaurapp.restaurapp.service.role.UpdateRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/role")
public class RoleController {
    private final CreateRoleService createRoleService;
    private final GetRolesService getRolesService;
    private final UpdateRoleService updateRoleService;
    private final DeleteRoleService deleteRoleService;

    public RoleController(CreateRoleService createRoleService, GetRolesService getRolesService,
                          UpdateRoleService updateRoleService, DeleteRoleService deleteRoleService) {
        this.createRoleService = createRoleService;
        this.getRolesService = getRolesService;
        this.updateRoleService = updateRoleService;
        this.deleteRoleService = deleteRoleService;
    }

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Role role){
        createRoleService.execute(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @GetMapping
    public ResponseEntity<List<Role>> getRoles(){
        return ResponseEntity.status(HttpStatus.OK).body(getRolesService.execute());
    }

    @PutMapping
    public ResponseEntity<Role> update(@RequestBody Role role){
        updateRoleService.execute(role);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @DeleteMapping
    public ResponseEntity<Role> delete(@RequestBody Role role){
        deleteRoleService.execute(role);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }
}
