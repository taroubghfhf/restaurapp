package com.restaurapp.restaurapp.service.table;

import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.model.Table;
import com.restaurapp.restaurapp.domain.repository.TableRepositoryJpa;
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
class GetTableServiceTest {

    @Mock
    private TableRepositoryJpa tableRepositoryJpa;

    @InjectMocks
    private GetTableService getTableService;

    @Test
    void execute_WhenTablesExist_ShouldReturnAllTables() {
        // Given
        Status status = new Status(1, "AVAILABLE");
        Table table1 = new Table(1, 4, 1, status);
        Table table2 = new Table(2, 6, 2, status);
        List<Table> expectedTables = Arrays.asList(table1, table2);
        
        when(tableRepositoryJpa.findAll()).thenReturn(expectedTables);

        // When
        List<Table> result = getTableService.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(4, result.get(0).getCapacity());
        assertEquals(6, result.get(1).getCapacity());
        verify(tableRepositoryJpa, times(1)).findAll();
    }

    @Test
    void execute_WhenNoTablesExist_ShouldReturnEmptyList() {
        // Given
        when(tableRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Table> result = getTableService.execute();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tableRepositoryJpa, times(1)).findAll();
    }
}

