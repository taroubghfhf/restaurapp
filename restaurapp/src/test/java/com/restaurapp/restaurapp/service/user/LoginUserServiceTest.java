package com.restaurapp.restaurapp.service.user;

import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.model.UserDetailsImpl;
import com.restaurapp.restaurapp.domain.repository.UserRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserServiceTest {

    @Mock
    private UserRepositoryJpa userRepositoryJpa;

    @InjectMocks
    private LoginUserService loginUserService;

    private User user;

    @BeforeEach
    void setUp() {
        Role role = new Role(1, "ADMIN");
        user = new User(1, "Juan", "juan@example.com", "encodedPassword", role);
    }

    @Test
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        // Given
        when(userRepositoryJpa.findByEmail(anyString())).thenReturn(user);

        // When
        UserDetails result = loginUserService.loadUserByUsername("juan@example.com");

        // Then
        assertNotNull(result);
        assertTrue(result instanceof UserDetailsImpl);
        assertEquals("juan@example.com", result.getUsername());
        verify(userRepositoryJpa, times(2)).findByEmail("juan@example.com");
    }

    @Test
    void loadUserByUsername_WhenUserDoesNotExist_ShouldThrowException() {
        // Given
        when(userRepositoryJpa.findByEmail(anyString())).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginUserService.loadUserByUsername("nonexistent@example.com");
        });

        assertEquals("El usuario no existe", exception.getMessage());
        verify(userRepositoryJpa, times(1)).findByEmail("nonexistent@example.com");
    }
}

