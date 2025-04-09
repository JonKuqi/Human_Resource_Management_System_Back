package com.hrms.Human_Resource_Management_System_Back.controller.tenant;

import com.hrms.Human_Resource_Management_System_Back.controller.BaseController;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Contract;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.ContractService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contracts")
@AllArgsConstructor
public class ContractController extends BaseController<Contract, Integer> {
    private final ContractService svc;

    @Override
    protected ContractService getService() {
        return svc;
    }
}
