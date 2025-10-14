package com.restaurapp.restaurapp.service.status;

import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.repository.StatusRepositoryJpa;
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
class CreateStatusServiceTest {

    @Mock
    private StatusRepositoryJpa statusRepositoryJpa;

    @InjectMocks
    private CreateStatusService createStatusService;

    private Status status;

    @BeforeEach
    void setUp() {
        status = new Status();
        status.setStatusId(1);
        status.setName("ACTIVE");
    }

    @Test
    void execute_WhenStatusDoesNotExist_ShouldCreateStatus() {
        // Given
        when(statusRepositoryJpa.findByName(anyString())).thenReturn(null);
        when(statusRepositoryJpa.save(any(Status.class))).thenReturn(status);

        // When
        createStatusService.execute(status);

        // Then
        verify(statusRepositoryJpa, times(1)).findByName("ACTIVE");
        verify(statusRepositoryJpa, times(1)).save(status);
    }

    @Test
    void execute_WhenStatusExists_ShouldThrowException() {
        // Given
        when(statusRepositoryJpa.findByName(anyString())).thenReturn(status);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            createStatusService.execute(status);
        });

        assertEquals("El nombre del estado ya existe", exception.getMessage());
        verify(statusRepositoryJpa, times(1)).findByName("ACTIVE");
        verify(statusRepositoryJpa, never()).save(any(Status.class));
    }
}

