package com.restaurapp.restaurapp.service.user;

import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.repository.UserRepositoryJpa;

import java.util.List;

public class GetUserService {
    private final UserRepositoryJpa userRepositoryJpa;

    public GetUserService(UserRepositoryJpa userRepositoryJpa) {
        this.userRepositoryJpa = userRepositoryJpa;
    }
    public List<User> execute(){
        return this.userRepositoryJpa.findAll();
    }
}
