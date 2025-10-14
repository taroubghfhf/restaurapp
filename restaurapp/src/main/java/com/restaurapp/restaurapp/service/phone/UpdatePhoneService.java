package com.restaurapp.restaurapp.service.phone;

import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.domain.repository.PhoneRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class UpdatePhoneService {
    private final PhoneRepositoryJpa phoneRepositoryJpa;

    public UpdatePhoneService(PhoneRepositoryJpa phoneRepositoryJpa) {
        this.phoneRepositoryJpa = phoneRepositoryJpa;
    }

    @Transactional
    public void execute(Phone phone) {
        if (phoneRepositoryJpa.findById(phone.getPhoneId() * 1L).isEmpty()) {
            throw new RuntimeException("El numero de telefono no existe");
        }
        phoneRepositoryJpa.save(phone);
    }
}
