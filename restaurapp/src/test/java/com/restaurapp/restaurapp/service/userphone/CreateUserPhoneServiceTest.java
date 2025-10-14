package com.restaurapp.restaurapp.service.userphone;

import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.model.UserPhone;
import com.restaurapp.restaurapp.domain.repository.PhoneRepositoryJpa;
import com.restaurapp.restaurapp.domain.repository.UserPhoneRepositoryJpa;
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
class CreateUserPhoneServiceTest {

    @Mock
    private PhoneRepositoryJpa phoneRepositoryJpa;

    @Mock
    private UserRepositoryJpa userRepositoryJpa;

    @Mock
    private UserPhoneRepositoryJpa userPhoneRepositoryJpa;

    @InjectMocks
    private CreateUserPhoneService createUserPhoneService;

    private UserPhone userPhone;
    private User user;
    private Phone phone;

    @BeforeEach
    void setUp() {
        Role role = new Role(1, "USER");
        user = new User(1, "Juan", "juan@example.com", "password", role);
        phone = new Phone(1, "3001234567");
        
        userPhone = new UserPhone();
        userPhone.setUserPhoneId(1);
        userPhone.setUser(user);
        userPhone.setPhone(phone);
    }

    @Test
    void execute_WhenUserAndPhoneExist_ShouldCreateUserPhone() {
        // Given
        doReturn(Optional.of(user)).when(userRepositoryJpa).findById(anyLong());
        doReturn(Optional.of(phone)).when(phoneRepositoryJpa).findById(anyLong());
        when(userPhoneRepositoryJpa.save(any(UserPhone.class))).thenReturn(userPhone);

        // When
        createUserPhoneService.execute(userPhone);

        // Then
        verify(userRepositoryJpa, times(1)).findById(1L);
        verify(phoneRepositoryJpa, times(1)).findById(1L);
        verify(userPhoneRepositoryJpa, times(1)).save(userPhone);
    }

    @Test
    void execute_WhenUserDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(userRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createUserPhoneService.execute(userPhone);
        });

        assertEquals("El ususario no existe", exception.getMessage());
        verify(userRepositoryJpa, times(1)).findById(1L);
        verify(phoneRepositoryJpa, never()).findById(anyLong());
        verify(userPhoneRepositoryJpa, never()).save(any(UserPhone.class));
    }

    @Test
    void execute_WhenPhoneDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.of(user)).when(userRepositoryJpa).findById(anyLong());
        doReturn(Optional.empty()).when(phoneRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createUserPhoneService.execute(userPhone);
        });

        assertEquals("El numero no existe", exception.getMessage());
        verify(userRepositoryJpa, times(1)).findById(1L);
        verify(phoneRepositoryJpa, times(1)).findById(1L);
        verify(userPhoneRepositoryJpa, never()).save(any(UserPhone.class));
    }
}

