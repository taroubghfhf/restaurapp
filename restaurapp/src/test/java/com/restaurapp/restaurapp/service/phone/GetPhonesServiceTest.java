package com.restaurapp.restaurapp.service.phone;

import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.domain.repository.PhoneRepositoryJpa;
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
class GetPhonesServiceTest {

    @Mock
    private PhoneRepositoryJpa phoneRepositoryJpa;

    @InjectMocks
    private GetPhonesService getPhonesService;

    @Test
    void execute_WhenPhonesExist_ShouldReturnAllPhones() {
        // Given
        Phone phone1 = new Phone(1, "3001234567");
        Phone phone2 = new Phone(2, "3107654321");
        List<Phone> expectedPhones = Arrays.asList(phone1, phone2);
        
        when(phoneRepositoryJpa.findAll()).thenReturn(expectedPhones);

        // When
        List<Phone> result = getPhonesService.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("3001234567", result.get(0).getNumber());
        assertEquals("3107654321", result.get(1).getNumber());
        verify(phoneRepositoryJpa, times(1)).findAll();
    }

    @Test
    void execute_WhenNoPhonesExist_ShouldReturnEmptyList() {
        // Given
        when(phoneRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Phone> result = getPhonesService.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(phoneRepositoryJpa, times(1)).findAll();
    }
}

