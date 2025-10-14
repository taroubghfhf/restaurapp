package com.restaurapp.restaurapp.controller.userphone;

import com.restaurapp.restaurapp.domain.model.UserPhone;
import com.restaurapp.restaurapp.service.userphone.CreateUserPhoneService;
import com.restaurapp.restaurapp.service.userphone.DeleteUserPhoneService;
import com.restaurapp.restaurapp.service.userphone.GetUserPhoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user-phone")
public class UserPhoneController {
    private final CreateUserPhoneService createUserPhoneService;
    private final GetUserPhoneService getUserPhoneService;
    private final DeleteUserPhoneService deleteUserPhoneService;

    public UserPhoneController(CreateUserPhoneService createUserPhoneService, GetUserPhoneService getUserPhoneService,
                              DeleteUserPhoneService deleteUserPhoneService) {
        this.createUserPhoneService = createUserPhoneService;
        this.getUserPhoneService = getUserPhoneService;
        this.deleteUserPhoneService = deleteUserPhoneService;
    }

    @PostMapping
    public ResponseEntity<UserPhone> create(@RequestBody UserPhone userPhone) {
        createUserPhoneService.execute(userPhone);
        return ResponseEntity.status(HttpStatus.CREATED).body(userPhone);
    }

    @GetMapping
    public ResponseEntity<List<UserPhone>> getUserPhone(){
        return ResponseEntity.status(HttpStatus.OK).body(getUserPhoneService.execute());
    }

    @DeleteMapping
    public ResponseEntity<UserPhone> delete(@RequestBody UserPhone userPhone){
        deleteUserPhoneService.execute(userPhone);
        return ResponseEntity.status(HttpStatus.OK).body(userPhone);
    }

}
