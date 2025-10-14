package com.restaurapp.restaurapp.service.status;

import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.repository.StatusRepositoryJpa;
import jakarta.transaction.Transactional;

public class CreateStatusService {

    private final StatusRepositoryJpa statusRepositoryJpa;

    public CreateStatusService(StatusRepositoryJpa statusRepositoryJpa) {
        this.statusRepositoryJpa = statusRepositoryJpa;
    }

    @Transactional
    public void execute(Status status) {
        if (statusRepositoryJpa.findByName(status.getName()) != null) {
            throw new RuntimeException("El nombre del estado ya existe");
        }
        statusRepositoryJpa.save(status);

    }
}