package com.restaurapp.restaurapp.service.userphone;

import com.restaurapp.restaurapp.domain.model.UserPhone;
import com.restaurapp.restaurapp.domain.repository.PhoneRepositoryJpa;
import com.restaurapp.restaurapp.domain.repository.UserPhoneRepositoryJpa;
import com.restaurapp.restaurapp.domain.repository.UserRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class CreateUserPhoneService {
    private final PhoneRepositoryJpa phoneRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;
    private final UserPhoneRepositoryJpa userPhoneRepositoryJpa;

    public CreateUserPhoneService(PhoneRepositoryJpa phoneRepositoryJpa, UserRepositoryJpa userRepositoryJpa,
                                  UserPhoneRepositoryJpa userPhoneRepositoryJpa) {
        this.phoneRepositoryJpa = phoneRepositoryJpa;
        this.userRepositoryJpa = userRepositoryJpa;
        this.userPhoneRepositoryJpa = userPhoneRepositoryJpa;
    }

    @Transactional
    public void execute(UserPhone userPhone) {
        validateUser(userPhone.getUser().getUserId());
        validatePhone(userPhone.getPhone().getPhoneId());
        userPhoneRepositoryJpa.save(userPhone);
    }

    private void validateUser(int userId) {
        if (userRepositoryJpa.findById(userId * 1L).isEmpty()) {
            throw new RuntimeException("El ususario no existe");
        }
    }

    private void validatePhone(int phoneId) {
        if (phoneRepositoryJpa.findById(phoneId * 1L).isEmpty()) {
            throw new RuntimeException("El numero no existe");
        }
    }
}
