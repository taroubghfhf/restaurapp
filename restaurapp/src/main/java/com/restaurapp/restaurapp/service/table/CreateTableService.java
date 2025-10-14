package com.restaurapp.restaurapp.service.table;

import com.restaurapp.restaurapp.domain.model.Table;
import com.restaurapp.restaurapp.domain.repository.TableRepositoryJpa;
import jakarta.transaction.Transactional;

public class CreateTableService {

    private final TableRepositoryJpa tableRepositoryJpa;


    public CreateTableService(TableRepositoryJpa tableRepositoryJpa) {
        this.tableRepositoryJpa = tableRepositoryJpa;
    }

    @Transactional
    public void execute(Table table) {
        tableRepositoryJpa.save(table);
    }
}
