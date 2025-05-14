package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Address;
import com.hrms.Human_Resource_Management_System_Back.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling business logic related to addresses.
 * <p> This class extends {@link BaseService} and provides methods for managing address entities, including
 * interacting with the underlying repository.</p>
 */
@Service
@AllArgsConstructor
public class AddressService extends BaseService<Address, Integer> {

    /**
     * The repository for performing CRUD operations on address entities.
     */
    private final AddressRepository addressRepository;

    /**
     * Returns the address repository.
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository for address entities.
     * @return the address repository
     */
    @Override
    protected AddressRepository getRepository() {
        return addressRepository;
    }
}