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
import static org.mockito.ArgumentMatchers.anyLong;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePhoneServiceTest {

    @Mock
    private PhoneRepositoryJpa phoneRepositoryJpa;

    @InjectMocks
    private UpdatePhoneService updatePhoneService;

    private Phone phone;

    @BeforeEach
    void setUp() {
        phone = new Phone();
        phone.setPhoneId(1);
        phone.setNumber("3001234567");
    }

    @Test
    void execute_WhenPhoneExists_ShouldUpdatePhone() {
        // Given
        doReturn(Optional.of(phone)).when(phoneRepositoryJpa).findById(anyLong());
        when(phoneRepositoryJpa.save(any(Phone.class))).thenReturn(phone);

        // When
        updatePhoneService.execute(phone);

        // Then
        verify(phoneRepositoryJpa, times(1)).findById(1L);
        verify(phoneRepositoryJpa, times(1)).save(phone);
    }

    @Test
    void execute_WhenPhoneDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(phoneRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updatePhoneService.execute(phone);
        });

        assertEquals("El numero de telefono no existe", exception.getMessage());
        verify(phoneRepositoryJpa, times(1)).findById(1L);
        verify(phoneRepositoryJpa, never()).save(any(Phone.class));
    }
}

