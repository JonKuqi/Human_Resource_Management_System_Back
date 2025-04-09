package com.hrms.Human_Resource_Management_System_Back.controller.tenant;

import com.hrms.Human_Resource_Management_System_Back.controller.BaseController;
import com.hrms.Human_Resource_Management_System_Back.model.Address;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/addresses")
@AllArgsConstructor
public class AddressController extends BaseController<Address, Integer> {
    private final AddressService svc;

    @Override
    protected AddressService getService() {
        return svc;
    }
}
