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
class DeleteStatusServiceTest {

    @Mock
    private StatusRepositoryJpa statusRepositoryJpa;

    @InjectMocks
    private DeleteStatusService deleteStatusService;

    private Status status;

    @BeforeEach
    void setUp() {
        status = new Status();
        status.setStatusId(1);
        status.setName("ACTIVE");
    }

    @Test
    void execute_WhenStatusExists_ShouldDeleteStatus() {
        // Given
        doReturn(Optional.of(status)).when(statusRepositoryJpa).findById(anyLong());
        doNothing().when(statusRepositoryJpa).delete(any(Status.class));

        // When
        deleteStatusService.execute(status);

        // Then
        verify(statusRepositoryJpa, times(1)).findById(1L);
        verify(statusRepositoryJpa, times(1)).delete(status);
    }

    @Test
    void execute_WhenStatusDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(statusRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteStatusService.execute(status);
        });

        assertEquals("El estado no existe", exception.getMessage());
        verify(statusRepositoryJpa, times(1)).findById(1L);
        verify(statusRepositoryJpa, never()).delete(any(Status.class));
    }
}

