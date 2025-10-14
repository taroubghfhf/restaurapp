package com.restaurapp.restaurapp.service.phone;

import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.domain.repository.PhoneRepositoryJpa;

import java.util.List;

public class GetPhonesService {

    private final PhoneRepositoryJpa phoneRepositoryJpa;

    public GetPhonesService(PhoneRepositoryJpa phoneRepositoryJpa) {
        this.phoneRepositoryJpa = phoneRepositoryJpa;
    }

    public List<Phone> execute(){
        return phoneRepositoryJpa.findAll();
    }
}
