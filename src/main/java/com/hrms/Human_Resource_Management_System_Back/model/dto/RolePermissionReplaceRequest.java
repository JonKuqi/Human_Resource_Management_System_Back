package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RolePermissionReplaceRequest {
    private List<Integer> permissionIds;   // same length
    private List<Integer> targetRoleIds;   // use 0 for ALL → service stores null
}