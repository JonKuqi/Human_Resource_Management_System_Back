package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserRoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRoleService extends BaseService<UserRole, Integer> {
    private final UserRoleRepository repo;
    private final UserTenantRepository tenantRepo;
    private final RoleRepository roleRepo;

    @Override
    protected UserRoleRepository getRepository() {
        return repo;
    }

    public List<UserRole> getUserRoles(Integer userId) {
        return repo.findByUserId(userId);
    }

    public List<UserRole> findByUserTenantId(Integer userTenantId) {
        return repo.findAllByUserTenant_UserTenantId(userTenantId);
    }

    @Transactional
    public void replaceRoles(Integer userTenantId, List<Integer> roleIds) {
        repo.deleteByUserTenantId(userTenantId);

        UserTenant ut = tenantRepo.getReferenceById(userTenantId);

        List<UserRole> fresh = roleIds.stream()
                .map(roleRepo::getReferenceById)
                .map(role -> UserRole.builder()
                        .userTenant(ut)
                        .role(role)
                        .build())
                .toList();

        repo.saveAll(fresh);
    }
}
