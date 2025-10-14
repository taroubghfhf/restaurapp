package com.restaurapp.restaurapp.service.role;

import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.repository.RoleRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRoleServiceTest {

    @Mock
    private RoleRepositoryJpa roleRepositoryJpa;

    @InjectMocks
    private CreateRoleService createRoleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1);
        role.setName("ADMIN");
    }

    @Test
    void execute_WhenRoleDoesNotExist_ShouldCreateRole() {
        // Given
        when(roleRepositoryJpa.findByName(anyString())).thenReturn(null);
        when(roleRepositoryJpa.save(any(Role.class))).thenReturn(role);

        // When
        createRoleService.execute(role);

        // Then
        verify(roleRepositoryJpa, times(1)).findByName("ADMIN");
        verify(roleRepositoryJpa, times(1)).save(role);
    }

    @Test
    void execute_WhenRoleExists_ShouldThrowException() {
        // Given
        when(roleRepositoryJpa.findByName(anyString())).thenReturn(role);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createRoleService.execute(role);
        });

        assertEquals("El role ya existe", exception.getMessage());
        verify(roleRepositoryJpa, times(1)).findByName("ADMIN");
        verify(roleRepositoryJpa, never()).save(any(Role.class));
    }
}

