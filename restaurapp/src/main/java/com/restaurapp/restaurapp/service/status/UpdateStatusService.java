package com.restaurapp.restaurapp.service.status;

import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.repository.StatusRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class UpdateStatusService {
    private final StatusRepositoryJpa statusRepositoryJpa;

    public UpdateStatusService(StatusRepositoryJpa statusRepositoryJpa) {
        this.statusRepositoryJpa = statusRepositoryJpa;
    }

    @Transactional
    public void execute(Status status) {
        if (statusRepositoryJpa.findById(status.getStatusId() * 1L).isEmpty()) {
            throw new RuntimeException("El estado no existe");
        }
        statusRepositoryJpa.save(status);
    }
}
