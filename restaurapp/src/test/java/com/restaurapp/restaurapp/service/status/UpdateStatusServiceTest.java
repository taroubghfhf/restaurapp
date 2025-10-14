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
import static org.mockito.ArgumentMatchers.anyLong;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateStatusServiceTest {

    @Mock
    private StatusRepositoryJpa statusRepositoryJpa;

    @InjectMocks
    private UpdateStatusService updateStatusService;

    private Status status;

    @BeforeEach
    void setUp() {
        status = new Status();
        status.setStatusId(1);
        status.setName("UPDATED");
    }

    @Test
    void execute_WhenStatusExists_ShouldUpdateStatus() {
        // Given
        doReturn(Optional.of(status)).when(statusRepositoryJpa).findById(anyLong());
        when(statusRepositoryJpa.save(any(Status.class))).thenReturn(status);

        // When
        updateStatusService.execute(status);

        // Then
        verify(statusRepositoryJpa, times(1)).findById(1L);
        verify(statusRepositoryJpa, times(1)).save(status);
    }

    @Test
    void execute_WhenStatusDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(statusRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            updateStatusService.execute(status);
        });

        assertEquals("El estado no existe", exception.getMessage());
        verify(statusRepositoryJpa, times(1)).findById(1L);
        verify(statusRepositoryJpa, never()).save(any(Status.class));
    }
}

