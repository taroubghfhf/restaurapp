package com.restaurapp.restaurapp.service.table;

import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.model.Table;
import com.restaurapp.restaurapp.domain.repository.TableRepositoryJpa;
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
class DeleteTableServiceTest {

    @Mock
    private TableRepositoryJpa tableRepositoryJpa;

    @InjectMocks
    private DeleteTableService deleteTableService;

    private Table table;

    @BeforeEach
    void setUp() {
        Status status = new Status(1, "AVAILABLE");
        table = new Table();
        table.setTableId(1);
        table.setCapacity(4);
        table.setLocation(1);
        table.setStatus(status);
    }

    @Test
    void execute_WhenTableExists_ShouldDeleteTable() {
        // Given
        doReturn(Optional.of(table)).when(tableRepositoryJpa).findById(anyLong());
        doNothing().when(tableRepositoryJpa).delete(any(Table.class));

        // When
        deleteTableService.execute(table);

        // Then
        verify(tableRepositoryJpa, times(1)).findById(1L);
        verify(tableRepositoryJpa, times(1)).delete(table);
    }

    @Test
    void execute_WhenTableDoesNotExist_ShouldThrowException() {
        // Given
        doReturn(Optional.empty()).when(tableRepositoryJpa).findById(anyLong());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteTableService.execute(table);
        });

        assertEquals("La mesa no existe", exception.getMessage());
        verify(tableRepositoryJpa, times(1)).findById(1L);
        verify(tableRepositoryJpa, never()).delete(any(Table.class));
    }
}

