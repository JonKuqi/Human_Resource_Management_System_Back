package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.dto.UserRolePermissionDto;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RolePermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class RolePermissionService extends BaseService<RolePermission, Integer> {
    private final RolePermissionRepository repo;

    @Override
    protected RolePermissionRepository getRepository() {
        return repo;
    }

    @Transactional
    public List<UserRolePermissionDto> getUserRolePermissions(Integer id) {
        List<UserRolePermissionDto> list =  repo.findScopedPermissionsByUserTenantId(id);

        HashMap<String, UserRolePermissionDto> hashMap = new HashMap<>();

        try {
            for (UserRolePermissionDto permission : list) {
                String toHash = permission.getVerb() + ":" + permission.getResource();
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = md.digest(toHash.getBytes(StandardCharsets.UTF_8));
                String hash = bytesToHex(hashBytes);

                String currentTargetRole = permission.getTarget_role();

                if (!hashMap.containsKey(hash)) {
                    hashMap.put(hash, permission);
                } else {
                    UserRolePermissionDto existing = hashMap.get(hash);
                    String existingTargetRole = existing.getTarget_role();

                    // Prefer the one with null (global) scope
                    if (existingTargetRole != null && currentTargetRole == null) {
                        hashMap.put(hash, permission);
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(hashMap.values());
    }
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
