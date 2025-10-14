package com.restaurapp.restaurapp.service.role;

import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.repository.RoleRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRolesServiceTest {

    @Mock
    private RoleRepositoryJpa roleRepositoryJpa;

    @InjectMocks
    private GetRolesService getRolesService;

    @Test
    void execute_WhenRolesExist_ShouldReturnAllRoles() {
        // Given
        Role role1 = new Role(1, "ADMIN");
        Role role2 = new Role(2, "USER");
        List<Role> expectedRoles = Arrays.asList(role1, role2);
        
        when(roleRepositoryJpa.findAll()).thenReturn(expectedRoles);

        // When
        List<Role> result = getRolesService.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getName());
        assertEquals("USER", result.get(1).getName());
        verify(roleRepositoryJpa, times(1)).findAll();
    }

    @Test
    void execute_WhenNoRolesExist_ShouldReturnEmptyList() {
        // Given
        when(roleRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Role> result = getRolesService.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roleRepositoryJpa, times(1)).findAll();
    }
}

