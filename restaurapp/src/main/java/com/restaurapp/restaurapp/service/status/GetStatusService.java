package com.restaurapp.restaurapp.service.status;

import com.restaurapp.restaurapp.domain.model.Status;
import com.restaurapp.restaurapp.domain.repository.StatusRepositoryJpa;

import java.util.List;

public class GetStatusService {

    private final StatusRepositoryJpa statusRepositoryJpa;

    public GetStatusService(StatusRepositoryJpa statusRepositoryJpa) {

        this.statusRepositoryJpa = statusRepositoryJpa;
    }

    public List<Status> execute(){
        return this.statusRepositoryJpa.findAll();
    }
}
