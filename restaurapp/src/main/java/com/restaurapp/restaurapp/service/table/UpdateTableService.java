package com.restaurapp.restaurapp.service.table;

import com.restaurapp.restaurapp.domain.model.Table;
import com.restaurapp.restaurapp.domain.repository.TableRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class UpdateTableService {
    private final TableRepositoryJpa tableRepositoryJpa;

    public UpdateTableService(TableRepositoryJpa tableRepositoryJpa) {
        this.tableRepositoryJpa = tableRepositoryJpa;
    }

    @Transactional
    public void execute(Table table) {
        if (tableRepositoryJpa.findById(table.getTableId() * 1L).isEmpty()) {
            throw new RuntimeException("La mesa no existe");
        }
        tableRepositoryJpa.save(table);
    }
}
