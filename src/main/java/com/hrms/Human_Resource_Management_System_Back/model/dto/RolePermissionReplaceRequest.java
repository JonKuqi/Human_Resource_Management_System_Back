package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RolePermissionReplaceRequest {
    private List<Integer> permissionIds;   // the selected check-box IDs
    private Integer targetRoleId;          // optional: same as roleId if N/A
}