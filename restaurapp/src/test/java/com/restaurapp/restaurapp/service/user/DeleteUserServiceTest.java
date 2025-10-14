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
class DeleteUserServiceTest {

    @Mock
    private UserRepositoryJpa userRepositoryJpa;

    @InjectMocks
    private DeleteUserService deleteUserService;

    private User user;

    @BeforeEach
    void setUp() {
        Role role = new Role(1, "ADMIN");
        user = new User();
        user.setName("Juan");
        user.setEmail("juan@example.com");
        user.setPassword("password123");
        user.setRole(role);
    }

    @Test
    void execute_WhenUserExists_ShouldDeleteUser() {
        // Given
        doReturn(Optional.of(user)).when(userRepositoryJpa).findById(anyLong());
        doNothing().when(userRepositoryJpa).delete(any(User.class));

        // When
        deleteUserService.execute(user);

        // Then
        verify(userRepositoryJpa, times(1)).findById(0L);
        verify(userRepositoryJpa, times(1)).delete(user);
    }

    @Test
    void execute_WhenUserDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(userRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteUserService.execute(user);
        });

        assertEquals("El usuario no existe", exception.getMessage());
        verify(userRepositoryJpa, times(1)).findById(0L);
        verify(userRepositoryJpa, never()).delete(any(User.class));
    }
}

