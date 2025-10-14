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

import static org.mockito.ArgumentMatchers.any;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTableServiceTest {

    @Mock
    private TableRepositoryJpa tableRepositoryJpa;

    @InjectMocks
    private CreateTableService createTableService;

    private Table table;
    private Status status;

    @BeforeEach
    void setUp() {
        status = new Status(1, "AVAILABLE");
        table = new Table();
        table.setTableId(1);
        table.setCapacity(4);
        table.setLocation(1);
        table.setStatus(status);
    }

    @Test
    void execute_ShouldCreateTable() {
        // Given
        when(tableRepositoryJpa.save(any(Table.class))).thenReturn(table);

        // When
        createTableService.execute(table);

        // Then
        verify(tableRepositoryJpa, times(1)).save(table);
    }
}

