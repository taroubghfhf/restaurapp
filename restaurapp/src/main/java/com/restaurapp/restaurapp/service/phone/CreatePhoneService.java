package com.restaurapp.restaurapp.service.phone;

import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.domain.repository.PhoneRepositoryJpa;
import jakarta.transaction.Transactional;

public class CreatePhoneService {

    private final PhoneRepositoryJpa phoneRepositoryJpa;

    public CreatePhoneService(PhoneRepositoryJpa phoneRepositoryJpa) {
        this.phoneRepositoryJpa = phoneRepositoryJpa;
    }

    @Transactional
    public void execute(Phone phone) {
        if (phoneRepositoryJpa.findByNumber(phone.getNumber()) != null) {
            throw new RuntimeException("El numero del telefono ya existe");
        }
        phoneRepositoryJpa.save(phone);
    }
}
