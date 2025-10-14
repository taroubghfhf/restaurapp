package com.restaurapp.restaurapp.service.user;

import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.repository.UserRepositoryJpa;
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
class GetUserServiceTest {

    @Mock
    private UserRepositoryJpa userRepositoryJpa;

    @InjectMocks
    private GetUserService getUserService;

    @Test
    void execute_WhenUsersExist_ShouldReturnAllUsers() {
        // Given
        Role role = new Role(1, "ADMIN");
        User user1 = new User(1, "Juan", "juan@example.com", "password123", role);
        User user2 = new User(2, "Maria", "maria@example.com", "password456", role);
        List<User> expectedUsers = Arrays.asList(user1, user2);
        
        when(userRepositoryJpa.findAll()).thenReturn(expectedUsers);

        // When
        List<User> result = getUserService.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getName());
        assertEquals("Maria", result.get(1).getName());
        verify(userRepositoryJpa, times(1)).findAll();
    }

    @Test
    void execute_WhenNoUsersExist_ShouldReturnEmptyList() {
        // Given
        when(userRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // When
        List<User> result = getUserService.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepositoryJpa, times(1)).findAll();
    }
}

