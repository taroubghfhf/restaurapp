package com.restaurapp.restaurapp.controller.status;

import com.restaurapp.restaurapp.domain.model.Role;
import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.service.role.CreateRoleService;
import com.restaurapp.restaurapp.service.status.CreateStatusService;
import com.restaurapp.restaurapp.service.status.DeleteStatusService;
import com.restaurapp.restaurapp.service.status.GetStatusService;
import com.restaurapp.restaurapp.service.status.UpdateStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/status")
public class StatusController {
    private final CreateStatusService createStatusService;
    private final GetStatusService getStatusService;
    private final UpdateStatusService updateStatusService;
    private final DeleteStatusService deleteStatusService;

    public StatusController(CreateStatusService createStatusService, GetStatusService getStatusService,
                            UpdateStatusService updateStatusService, DeleteStatusService deleteStatusService) {
        this.createStatusService = createStatusService;
        this.getStatusService = getStatusService;
        this.updateStatusService = updateStatusService;
        this.deleteStatusService = deleteStatusService;
    }

    @PostMapping
    public ResponseEntity<Status> create(@RequestBody Status status){
        createStatusService.execute(status);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    @GetMapping
    public ResponseEntity<List<Status>> getStatus(){
        return ResponseEntity.status(HttpStatus.OK).body(getStatusService.execute());
    }

    @PutMapping
    public ResponseEntity<Status> updateStatus(@RequestBody Status status){
        updateStatusService.execute(status);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @DeleteMapping
    public ResponseEntity<Status> delete(@RequestBody Status status){
        deleteStatusService.execute(status);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
}
