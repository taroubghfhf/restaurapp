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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

    @Mock
    private UserRepositoryJpa userRepositoryJpa;

    @InjectMocks
    private UpdateUserService updateUserService;

    private User user;

    @BeforeEach
    void setUp() {
        Role role = new Role(1, "ADMIN");
        user = new User();
        user.setName("Juan Actualizado");
        user.setEmail("juan@example.com");
        user.setPassword("newpassword");
        user.setRole(role);
    }

    @Test
    void execute_WhenUserExists_ShouldUpdateUser() {
        // Given
        doReturn(Optional.of(user)).when(userRepositoryJpa).findById(anyLong());
        when(userRepositoryJpa.save(any(User.class))).thenReturn(user);

        // When
        updateUserService.execute(user);

        // Then
        verify(userRepositoryJpa, times(1)).findById(0L);
        verify(userRepositoryJpa, times(1)).save(user);
    }

    @Test
    void execute_WhenUserDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(userRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateUserService.execute(user);
        });

        assertEquals("El usuario no existe", exception.getMessage());
        verify(userRepositoryJpa, times(1)).findById(0L);
        verify(userRepositoryJpa, never()).save(any(User.class));
    }
}

