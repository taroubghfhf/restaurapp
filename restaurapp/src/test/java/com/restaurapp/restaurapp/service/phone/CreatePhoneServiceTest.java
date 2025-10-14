package com.restaurapp.restaurapp.service.phone;

import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.domain.repository.PhoneRepositoryJpa;
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
class CreatePhoneServiceTest {

    @Mock
    private PhoneRepositoryJpa phoneRepositoryJpa;

    @InjectMocks
    private CreatePhoneService createPhoneService;

    private Phone phone;

    @BeforeEach
    void setUp() {
        phone = new Phone();
        phone.setPhoneId(1);
        phone.setNumber("3001234567");
    }

    @Test
    void execute_WhenPhoneDoesNotExist_ShouldCreatePhone() {
        // Given
        when(phoneRepositoryJpa.findByNumber(anyString())).thenReturn(null);
        when(phoneRepositoryJpa.save(any(Phone.class))).thenReturn(phone);

        // When
        createPhoneService.execute(phone);

        // Then
        verify(phoneRepositoryJpa, times(1)).findByNumber("3001234567");
        verify(phoneRepositoryJpa, times(1)).save(phone);
    }

    @Test
    void execute_WhenPhoneExists_ShouldThrowException() {
        // Given
        when(phoneRepositoryJpa.findByNumber(anyString())).thenReturn(phone);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createPhoneService.execute(phone);
        });

        assertEquals("El numero del telefono ya existe", exception.getMessage());
        verify(phoneRepositoryJpa, times(1)).findByNumber("3001234567");
        verify(phoneRepositoryJpa, never()).save(any(Phone.class));
    }
}

