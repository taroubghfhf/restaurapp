package com.restaurapp.restaurapp.controller.table;

import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.model.Table;
import com.restaurapp.restaurapp.service.status.GetStatusService;
import com.restaurapp.restaurapp.service.table.CreateTableService;
import com.restaurapp.restaurapp.service.table.DeleteTableService;
import com.restaurapp.restaurapp.service.table.GetTableService;
import com.restaurapp.restaurapp.service.table.UpdateTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/table")
public class TableController {
    private final CreateTableService createTableService;
    private final GetTableService getTableService;
    private final UpdateTableService updateTableService;
    private final DeleteTableService deleteTableService;

    public TableController(CreateTableService createTableService, GetTableService getTableService,
                           UpdateTableService updateTableService, DeleteTableService deleteTableService) {
        this.createTableService = createTableService;
        this.getTableService = getTableService;
        this.updateTableService = updateTableService;
        this.deleteTableService = deleteTableService;
    }

    @PostMapping
    public ResponseEntity<Table> create(@RequestBody Table table){
        createTableService.execute(table);
        return ResponseEntity.status(HttpStatus.CREATED).body(table);
    }
    @GetMapping
    public ResponseEntity<List<Table>> getTable(){
        return ResponseEntity.status(HttpStatus.OK).body(getTableService.execute());
    }
    @PutMapping
    public ResponseEntity<Table> updateTable(@RequestBody Table table){
        updateTableService.execute(table);
        return ResponseEntity.status(HttpStatus.OK).body(table);
    }

    @DeleteMapping
    public ResponseEntity<Table> delete(@RequestBody Table table){
        deleteTableService.execute(table);
        return ResponseEntity.status(HttpStatus.OK).body(table);
    }
}
