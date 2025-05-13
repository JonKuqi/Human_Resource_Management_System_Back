package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) for user role permissions.
 * <p>
 * - name: The name of the permission.
 * - verb: The HTTP verb associated with the permission (e.g., GET, POST).
 * - resource: The resource to which the permission applies.
 * - target_role: The target role associated with the permission, if any.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRolePermissionDto {
    String name;
    String verb;
    String resource;
    String target_role;
}
