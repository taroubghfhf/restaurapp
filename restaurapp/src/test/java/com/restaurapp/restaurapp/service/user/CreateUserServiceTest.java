package com.restaurapp.restaurapp.service.user;

import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.repository.UserRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @Mock
    private UserRepositoryJpa userRepositoryJpa;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserService createUserService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(1, "ADMIN");
        user = new User();
        user.setName("Juan");
        user.setEmail("juan@example.com");
        user.setPassword("password123");
        user.setRole(role);
    }

    @Test
    void execute_WhenUserDoesNotExist_ShouldCreateUser() {
        // Given
        when(userRepositoryJpa.findByName(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepositoryJpa.save(any(User.class))).thenReturn(user);

        // When
        createUserService.execute(user);

        // Then
        verify(userRepositoryJpa, times(1)).findByName("Juan");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepositoryJpa, times(1)).save(user);
    }

    @Test
    void execute_WhenUserExists_ShouldThrowException() {
        // Given
        when(userRepositoryJpa.findByName(anyString())).thenReturn(user);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createUserService.execute(user);
        });

        assertEquals("El usuario ya existe", exception.getMessage());
        verify(userRepositoryJpa, times(1)).findByName("Juan");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepositoryJpa, never()).save(any(User.class));
    }

    @Test
    void execute_WhenPasswordIsEmpty_ShouldThrowException() {
        // Given
        user.setPassword("");
        when(userRepositoryJpa.findByName(anyString())).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createUserService.execute(user);
        });

        assertEquals("La contraseña no puede estar vacía", exception.getMessage());
        verify(userRepositoryJpa, times(1)).findByName("Juan");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepositoryJpa, never()).save(any(User.class));
    }

    @Test
    void execute_WhenPasswordIsNull_ShouldThrowException() {
        // Given
        user.setPassword(null);
        when(userRepositoryJpa.findByName(anyString())).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createUserService.execute(user);
        });

        assertEquals("La contraseña no puede estar vacía", exception.getMessage());
        verify(userRepositoryJpa, times(1)).findByName("Juan");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepositoryJpa, never()).save(any(User.class));
    }
}

