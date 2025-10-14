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
class DeleteRoleServiceTest {

    @Mock
    private RoleRepositoryJpa roleRepositoryJpa;

    @InjectMocks
    private DeleteRoleService deleteRoleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1);
        role.setName("ADMIN");
    }

    @Test
    void execute_WhenRoleExists_ShouldDeleteRole() {
        // Given
        doReturn(Optional.of(role)).when(roleRepositoryJpa).findById(anyLong());
        doNothing().when(roleRepositoryJpa).delete(any(Role.class));

        // When
        deleteRoleService.execute(role);

        // Then
        verify(roleRepositoryJpa, times(1)).findById(1L);
        verify(roleRepositoryJpa, times(1)).delete(role);
    }

    @Test
    void execute_WhenRoleDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(roleRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteRoleService.execute(role);
        });

        assertEquals("El rol no existe", exception.getMessage());
        verify(roleRepositoryJpa, times(1)).findById(1L);
        verify(roleRepositoryJpa, never()).delete(any(Role.class));
    }
}

