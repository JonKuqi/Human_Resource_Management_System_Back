package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.Address;
import com.hrms.Human_Resource_Management_System_Back.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/addresses")
@AllArgsConstructor
public class AddressController extends BaseController<Address, Integer> {

    private final AddressService addressService;

    @Override
    protected AddressService getService() {
        return addressService;
    }
}