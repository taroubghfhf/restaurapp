package com.restaurapp.restaurapp.controller.phone;

import com.restaurapp.restaurapp.domain.model.Category;
import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.service.category.CreateCategoryService;
import com.restaurapp.restaurapp.service.category.UpdateCategoryService;
import com.restaurapp.restaurapp.service.phone.CreatePhoneService;
import com.restaurapp.restaurapp.service.phone.DeletePhoneService;
import com.restaurapp.restaurapp.service.phone.GetPhonesService;
import com.restaurapp.restaurapp.service.phone.UpdatePhoneService;
import org.hibernate.query.UnknownParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/phone")
public class PhoneController {
    private final CreatePhoneService createPhoneService;
    private final GetPhonesService getPhonesService;
    private final UpdatePhoneService updatePhoneService;
    private final DeletePhoneService deletePhoneService;

    public PhoneController(CreatePhoneService createPhoneService, GetPhonesService getPhonesService,
                           UpdatePhoneService updatePhoneService, DeletePhoneService deletePhoneService) {
        this.createPhoneService = createPhoneService;
        this.getPhonesService = getPhonesService;
        this.updatePhoneService = updatePhoneService;
        this.deletePhoneService = deletePhoneService;
    }

    @PostMapping
    public ResponseEntity<Phone> create(@RequestBody Phone phone){
        createPhoneService.execute(phone);
        return ResponseEntity.status(HttpStatus.CREATED).body(phone);
    }

    @GetMapping
    public ResponseEntity<List<Phone>> getPhones(){
        return  ResponseEntity.status(HttpStatus.OK).body(getPhonesService.execute());
    }

    @PutMapping
    public ResponseEntity<Phone> update(@RequestBody Phone phone){
        updatePhoneService.execute(phone);
        return ResponseEntity.status(HttpStatus.OK).body(phone);
    }

    @DeleteMapping
    public ResponseEntity<Phone> deletePhone(@RequestBody Phone phone){
        deletePhoneService.execute(phone);
        return ResponseEntity.status(HttpStatus.OK).body(phone);
    }
}
