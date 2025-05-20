package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import com.hrms.Human_Resource_Management_System_Back.model.dto.RolePermissionReplaceRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.UserRolePermissionDto;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Role;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.RolePermission;
import com.hrms.Human_Resource_Management_System_Back.repository.TenantPermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RolePermissionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.RoleRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Service class for handling business logic related to role permissions.
 * <p>
 * This class extends {@link BaseService} and provides methods for managing role permissions,
 * including assigning permissions to roles, retrieving role-based permissions, and replacing permissions for a role.
 * </p>
 */
@Service
@AllArgsConstructor
public class RolePermissionService extends BaseService<RolePermission, Integer> {

    /**
     * The repository responsible for performing CRUD operations on role permission entities.
     */
    private final RolePermissionRepository repo;

    /**
     * The repository for managing role entities.
     */
    private final RoleRepository roleRepo;

    /**
     * The repository for managing tenant permission entities.
     */
    private final TenantPermissionRepository permRepo;

    /**
     * Returns the role permission repository.
     * <p>
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository
     * for role permission entities.
     * </p>
     *
     * @return the role permission repository
     */
    @Override
    protected RolePermissionRepository getRepository() {
        return repo;
    }

    /**
     * Retrieves a list of user role permissions associated with a specific user tenant ID.
     * <p>
     * This method fetches the role permissions for a given user tenant and processes them to ensure that duplicate permissions
     * with the same verb and resource are eliminated, preferring the one with a null (global) target role.
     * </p>
     *
     * @param id the user tenant ID to retrieve role permissions for
     * @return a list of {@link UserRolePermissionDto} representing the filtered and processed permissions for the user tenant
     */
    @Transactional
    @Cacheable(value = "user-role-permissions", key = "#id")
    public List<UserRolePermissionDto> getUserRolePermissions(Integer id) {
        List<UserRolePermissionDto> list = repo.findScopedPermissionsByUserTenantId(id);
        System.out.println("KALTRINAAAAA?????????!!!!!!!!!");

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

    /**
     * Converts a byte array into a hexadecimal string representation.
     * <p>
     * This method is used to generate a unique hash for a permission based on its verb and resource.
     * </p>
     *
     * @param bytes the byte array to convert
     * @return a hexadecimal string representing the byte array
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Retrieves a list of role permissions associated with a specific role ID.
     * <p>
     * This method fetches all role permissions for a given role by its ID.
     * </p>
     *
     * @param roleId the role ID to retrieve permissions for
     * @return a list of {@link RolePermission} associated with the given role ID
     */
    @Cacheable(value = "role-permissions", key = "#roleId")
    public List<RolePermission> findByRoleId(Integer roleId) {
        return repo.findAllByRole_RoleId(roleId);
    }

    /**
     * Replaces the permissions associated with a specific role.
     * <p>
     * This method first deletes the existing permissions for the given role and then assigns the new permissions
     * specified by the provided list of permission IDs and target role IDs.
     * </p>
     *
     * @param roleId the role ID whose permissions are to be replaced
     * @param req the request containing the new permission and target role IDs
     */
    @Transactional
    @CacheEvict(value = "user-role-permissions", key = "#roleId")
    public void replacePermissions(Integer roleId, RolePermissionReplaceRequest req) {
        repo.deleteByRoleId(roleId);   // JPQL @Modifying DELETE, inside Tx

        Role role = roleRepo.getReferenceById(roleId);
        List<Integer> targetList = req.getTargetRoleIds() != null
                ? req.getTargetRoleIds()
                : req.getPermissionIds().stream().map(i -> 0).toList();

        List<RolePermission> fresh = IntStream.range(0, req.getPermissionIds().size())
                .mapToObj(i -> {
                    Integer permId  = req.getPermissionIds().get(i);
                    Integer tgtId   = targetList.get(i);
                    TenantPermission perm = permRepo.getReferenceById(permId);
                    Role targetRole = tgtId != null && tgtId != 0
                            ? roleRepo.getReferenceById(tgtId)
                            : null;
                    return RolePermission.builder()
                            .role(role)
                            .tenantPermission(perm)
                            .targetRoleId(targetRole)
                            .build();
                })
                .toList();

        repo.saveAll(fresh);
    }
}