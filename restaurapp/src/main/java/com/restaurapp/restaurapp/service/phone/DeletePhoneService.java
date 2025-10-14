package com.restaurapp.restaurapp.service.phone;

import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.domain.repository.PhoneRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class DeletePhoneService {
    private final PhoneRepositoryJpa phoneRepositoryJpa;

    public DeletePhoneService(PhoneRepositoryJpa phoneRepositoryJpa) {
        this.phoneRepositoryJpa = phoneRepositoryJpa;
    }

    @Transactional
    public void execute(Phone phone) {
        if (this.phoneRepositoryJpa.findById(phone.getPhoneId() * 1L).isEmpty()) {
            throw new RuntimeException("El telefono no existe");
        }
        this.phoneRepositoryJpa.delete(phone);
    }
}
