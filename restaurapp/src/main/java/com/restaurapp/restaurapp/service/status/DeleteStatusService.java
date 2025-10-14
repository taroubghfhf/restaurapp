package com.restaurapp.restaurapp.service.status;

import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.repository.StatusRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class DeleteStatusService {
    private final StatusRepositoryJpa statusRepositoryJpa;

    public DeleteStatusService(StatusRepositoryJpa statusRepositoryJpa) {
        this.statusRepositoryJpa = statusRepositoryJpa;
    }

    @Transactional
    public void execute(Status status) {
        if (this.statusRepositoryJpa.findById(status.getStatusId() * 1L).isEmpty()) {
            throw new RuntimeException("El estado no existe");
        }
        this.statusRepositoryJpa.delete(status);
    }
}
