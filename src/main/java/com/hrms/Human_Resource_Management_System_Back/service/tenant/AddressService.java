package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.Address;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.AddressRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressService extends BaseService<Address, Integer> {
    private final AddressRepository repo;

    @Override
    protected AddressRepository getRepository() {
        return repo;
    }
}
