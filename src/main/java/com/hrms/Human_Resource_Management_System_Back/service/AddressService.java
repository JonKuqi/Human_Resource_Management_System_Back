package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Address;
import com.hrms.Human_Resource_Management_System_Back.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressService extends BaseService<Address, Integer> {

    private final AddressRepository addressRepository;

    @Override
    protected AddressRepository getRepository() {
        return addressRepository;
    }
}