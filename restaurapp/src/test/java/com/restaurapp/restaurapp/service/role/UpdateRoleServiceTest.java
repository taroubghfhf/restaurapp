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
import static org.mockito.ArgumentMatchers.anyLong;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRoleServiceTest {

    @Mock
    private RoleRepositoryJpa roleRepositoryJpa;

    @InjectMocks
    private UpdateRoleService updateRoleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1);
        role.setName("ADMIN_UPDATED");
    }

    @Test
    void execute_WhenRoleExists_ShouldUpdateRole() {
        // Given
        doReturn(Optional.of(role)).when(roleRepositoryJpa).findById(anyLong());
        when(roleRepositoryJpa.save(any(Role.class))).thenReturn(role);

        // When
        updateRoleService.execute(role);

        // Then
        verify(roleRepositoryJpa, times(1)).findById(1L);
        verify(roleRepositoryJpa, times(1)).save(role);
    }

    @Test
    void execute_WhenRoleDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(roleRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateRoleService.execute(role);
        });

        assertEquals("El rol no existe", exception.getMessage());
        verify(roleRepositoryJpa, times(1)).findById(1L);
        verify(roleRepositoryJpa, never()).save(any(Role.class));
    }
}

