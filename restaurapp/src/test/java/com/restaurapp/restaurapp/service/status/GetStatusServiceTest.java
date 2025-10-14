package com.restaurapp.restaurapp.service.status;

import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.repository.StatusRepositoryJpa;
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
class GetStatusServiceTest {

    @Mock
    private StatusRepositoryJpa statusRepositoryJpa;

    @InjectMocks
    private GetStatusService getStatusService;

    @Test
    void execute_WhenStatusesExist_ShouldReturnAllStatuses() {
        // Given
        Status status1 = new Status(1, "ACTIVE");
        Status status2 = new Status(2, "INACTIVE");
        List<Status> expectedStatuses = Arrays.asList(status1, status2);
        
        when(statusRepositoryJpa.findAll()).thenReturn(expectedStatuses);

        // When
        List<Status> result = getStatusService.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ACTIVE", result.get(0).getName());
        assertEquals("INACTIVE", result.get(1).getName());
        verify(statusRepositoryJpa, times(1)).findAll();
    }

    @Test
    void execute_WhenNoStatusesExist_ShouldReturnEmptyList() {
        // Given
        when(statusRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Status> result = getStatusService.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statusRepositoryJpa, times(1)).findAll();
    }
}

