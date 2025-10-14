package com.restaurapp.restaurapp.service.user;

import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.repository.UserRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class DeleteUserService {
    private final UserRepositoryJpa userRepositoryJpa;

    public DeleteUserService(UserRepositoryJpa userRepositoryJpa) {
        this.userRepositoryJpa = userRepositoryJpa;
    }

    @Transactional
    public void execute(User user) {
        if (this.userRepositoryJpa.findById(user.getUserId() * 1L).isEmpty()) {
            throw new RuntimeException("El usuario no existe");
        }
        this.userRepositoryJpa.delete(user);
    }
}
