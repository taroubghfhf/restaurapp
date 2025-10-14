package com.restaurapp.restaurapp.service.user;

import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.model.UserDetailsImpl;
import com.restaurapp.restaurapp.domain.repository.UserRepositoryJpa;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService implements UserDetailsService {

    private final UserRepositoryJpa userRepositoryJpa;

    public LoginUserService(UserRepositoryJpa userRepositoryJpa) {
        this.userRepositoryJpa = userRepositoryJpa;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userRepositoryJpa.findByEmail(email) == null) {
            throw new RuntimeException("El usuario no existe");
        }
        User user = userRepositoryJpa.findByEmail(email);
        return new UserDetailsImpl(user);
    }
}