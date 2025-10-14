package com.restaurapp.restaurapp.service.userphone;

import com.restaurapp.restaurapp.domain.model.UserPhone;
import com.restaurapp.restaurapp.domain.repository.UserPhoneRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class DeleteUserPhoneService {
    private final UserPhoneRepositoryJpa userPhoneRepositoryJpa;

    public DeleteUserPhoneService(UserPhoneRepositoryJpa userPhoneRepositoryJpa) {
        this.userPhoneRepositoryJpa = userPhoneRepositoryJpa;
    }

    @Transactional
    public void execute(UserPhone userPhone) {
        if (this.userPhoneRepositoryJpa.findById(userPhone.getUserPhoneId() * 1L).isEmpty()) {
            throw new RuntimeException("El telefono de usuario no existe");
        }
        this.userPhoneRepositoryJpa.delete(userPhone);
    }
}
