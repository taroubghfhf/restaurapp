package com.restaurapp.restaurapp.service.userphone;

import com.restaurapp.restaurapp.domain.model.UserPhone;
import com.restaurapp.restaurapp.domain.repository.UserPhoneRepositoryJpa;

import java.util.List;

public class GetUserPhoneService {
    private final UserPhoneRepositoryJpa userPhoneRepositoryJpa;

    public GetUserPhoneService(UserPhoneRepositoryJpa userPhoneRepositoryJpa) {
        this.userPhoneRepositoryJpa = userPhoneRepositoryJpa;
    }
    public List<UserPhone> execute(){
      return this.userPhoneRepositoryJpa.findAll();
    }
}
