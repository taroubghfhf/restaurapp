package com.restaurapp.restaurapp.service.user;

import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.repository.UserRepositoryJpa;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserService.class);

    private final UserRepositoryJpa userRepositoryJpa;
    private final PasswordEncoder passwordEncoder;

    public CreateUserService(UserRepositoryJpa userRepositoryJpa, PasswordEncoder passwordEncoder) {
        this.userRepositoryJpa = userRepositoryJpa;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void execute(User user) {

        if (userRepositoryJpa.findByName(user.getName()) != null) {
            throw new RuntimeException("El usuario ya existe");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepositoryJpa.save(user);
    }
}
