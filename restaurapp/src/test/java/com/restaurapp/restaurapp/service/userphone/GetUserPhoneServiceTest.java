package com.restaurapp.restaurapp.service.userphone;

import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.model.UserPhone;
import com.restaurapp.restaurapp.domain.repository.UserPhoneRepositoryJpa;
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
class GetUserPhoneServiceTest {

    @Mock
    private UserPhoneRepositoryJpa userPhoneRepositoryJpa;

    @InjectMocks
    private GetUserPhoneService getUserPhoneService;

    @Test
    void execute_WhenUserPhonesExist_ShouldReturnAllUserPhones() {
        // Given
        Role role = new Role(1, "USER");
        User user = new User(1, "Juan", "juan@example.com", "password", role);
        Phone phone1 = new Phone(1, "3001234567");
        Phone phone2 = new Phone(2, "3107654321");
        
        UserPhone userPhone1 = new UserPhone(1, user, phone1);
        UserPhone userPhone2 = new UserPhone(2, user, phone2);
        List<UserPhone> expectedUserPhones = Arrays.asList(userPhone1, userPhone2);
        
        when(userPhoneRepositoryJpa.findAll()).thenReturn(expectedUserPhones);

        // When
        List<UserPhone> result = getUserPhoneService.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("3001234567", result.get(0).getPhone().getNumber());
        assertEquals("3107654321", result.get(1).getPhone().getNumber());
        verify(userPhoneRepositoryJpa, times(1)).findAll();
    }

    @Test
    void execute_WhenNoUserPhonesExist_ShouldReturnEmptyList() {
        // Given
        when(userPhoneRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UserPhone> result = getUserPhoneService.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userPhoneRepositoryJpa, times(1)).findAll();
    }
}

