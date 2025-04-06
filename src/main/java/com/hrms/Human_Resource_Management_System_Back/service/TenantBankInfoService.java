package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.TenantBankInfo;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantBankInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TenantBankInfoService extends BaseService<TenantBankInfo, Integer> {

    private final TenantBankInfoRepository tenantBankInfoRepository;

    @Override
    protected TenantBankInfoRepository getRepository() {
        return tenantBankInfoRepository;
    }
}