package com.restaurapp.restaurapp.controller.user;

import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.service.user.CreateUserService;
import com.restaurapp.restaurapp.service.user.DeleteUserService;
import com.restaurapp.restaurapp.service.user.GetUserService;
import com.restaurapp.restaurapp.service.user.UpdateUserService;
import org.springframework.boot.convert.PeriodUnit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private final CreateUserService createUserService;
    private final GetUserService getUserService;
    private final UpdateUserService updateUserService;
    private final DeleteUserService deleteUserService;
    public UserController(CreateUserService createUserService, GetUserService getUserService,
                          UpdateUserService updateUserService, DeleteUserService deleteUserService) {
        this.createUserService = createUserService;
        this.getUserService = getUserService;
        this.updateUserService = updateUserService;
        this.deleteUserService = deleteUserService;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user){
        createUserService.execute(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUser(){
        return ResponseEntity.status(HttpStatus.OK).body(getUserService.execute());
    }

    @PutMapping
    public ResponseEntity<User> updateStatus(@RequestBody User user){
        updateUserService.execute(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping
    public ResponseEntity<User> delete(@RequestBody User user){
        deleteUserService.execute(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
