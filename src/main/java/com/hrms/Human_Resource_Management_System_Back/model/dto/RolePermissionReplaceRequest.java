package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

import java.util.List;

/**
 * Data transfer object (DTO) for replacing role permissions.
 * <p>
 * - permissionIds: A list of permission IDs to be assigned to the role.
 * - targetRoleIds: A list of target role IDs for the permissions, use 0 for all roles (which is stored as null).
 * </p>
 */
@Data
public class RolePermissionReplaceRequest {
    private List<Integer> permissionIds;   // same length
    private List<Integer> targetRoleIds;   // use 0 for ALL → service stores null
}