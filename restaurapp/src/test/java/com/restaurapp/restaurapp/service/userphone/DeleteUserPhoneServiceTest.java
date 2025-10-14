package com.restaurapp.restaurapp.service.userphone;

import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.model.User;
import com.restaurapp.restaurapp.domain.model.UserPhone;
import com.restaurapp.restaurapp.domain.repository.UserPhoneRepositoryJpa;
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
class DeleteUserPhoneServiceTest {

    @Mock
    private UserPhoneRepositoryJpa userPhoneRepositoryJpa;

    @InjectMocks
    private DeleteUserPhoneService deleteUserPhoneService;

    private UserPhone userPhone;

    @BeforeEach
    void setUp() {
        Role role = new Role(1, "USER");
        User user = new User(1, "Juan", "juan@example.com", "password", role);
        Phone phone = new Phone(1, "3001234567");
        
        userPhone = new UserPhone();
        userPhone.setUserPhoneId(1);
        userPhone.setUser(user);
        userPhone.setPhone(phone);
    }

    @Test
    void execute_WhenUserPhoneExists_ShouldDeleteUserPhone() {
        // Given
        doReturn(Optional.of(userPhone)).when(userPhoneRepositoryJpa).findById(anyLong());
        doNothing().when(userPhoneRepositoryJpa).delete(any(UserPhone.class));

        // When
        deleteUserPhoneService.execute(userPhone);

        // Then
        verify(userPhoneRepositoryJpa, times(1)).findById(1L);
        verify(userPhoneRepositoryJpa, times(1)).delete(userPhone);
    }

    @Test
    void execute_WhenUserPhoneDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(userPhoneRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteUserPhoneService.execute(userPhone);
        });

        assertEquals("El telefono de usuario no existe", exception.getMessage());
        verify(userPhoneRepositoryJpa, times(1)).findById(1L);
        verify(userPhoneRepositoryJpa, never()).delete(any(UserPhone.class));
    }
}

