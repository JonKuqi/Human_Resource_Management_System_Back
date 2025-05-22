package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Contract;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.ContractService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenant/contracts")
@AllArgsConstructor
public class ContractController extends BaseController<Contract, Integer> {
    private final ContractService svc;

//    @Override
//    protected ContractService getServiceSpecific() {
//        return svc;
//    }

    @Override
    protected BaseService<Contract, Integer> getService() {
        return svc;
    }
}
