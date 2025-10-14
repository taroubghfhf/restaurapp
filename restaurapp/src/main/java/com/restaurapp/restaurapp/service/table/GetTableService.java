package com.restaurapp.restaurapp.service.table;

import com.restaurapp.restaurapp.domain.model.Table;
import com.restaurapp.restaurapp.domain.repository.TableRepositoryJpa;

import java.util.List;

public class GetTableService {
    private final TableRepositoryJpa tableRepositoryJpa;

    public GetTableService(TableRepositoryJpa tableRepositoryJpa) {
        this.tableRepositoryJpa = tableRepositoryJpa;
    }

    public List<Table> execute() {
        return this.tableRepositoryJpa.findAll();
    }
}
