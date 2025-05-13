package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.Address;
import com.hrms.Human_Resource_Management_System_Back.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing addresses.
 * <p>
 * This controller provides endpoints for handling address-related operations. It includes CRUD functionality
 * for addresses within the system.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/public/address")
@AllArgsConstructor
public class AddressController extends BaseController<Address, Integer> {

    /**
     * The service responsible for handling address-related business logic.
     */
    private final AddressService addressService;

    /**
     * Overrides {@link BaseController#getService()} to return the address service.
     *
     * @return the address service
     */
    @Override
    protected AddressService getService() {
        return addressService;
    }
}