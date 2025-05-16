package com.hrms.Human_Resource_Management_System_Back.model.dto;


import lombok.Data;

/**
 * Dto for creating a new employee by User Tenant Owner.
 *
 * This class encapsulates the necessary data required to create a new employee.
 * - User: Email address used for creating the base user in the public schema.
 * - UserTenant: Personal and contact information such as first name, last name, phone, and gender.
 * - Address: Country, city, street, and zip code of the employee’s address.
 * - Contract: Title of the position, department name, contract type, salary, and contract end date.
 */
@Data
public class CreateEmployeeRequest {

    // USER (public schema)
    private String email;

    // USER TENANT (tenant schema)
    private String firstName;
    private String lastName;
    private String phone;
    private String gender;

    // ADDRESS (public schema)
    private String country;
    private String city;
    private String street;
    private String zip;

    // CONTRACT (tenant schema)
    private String positionTitle;
    private String departmentName;
    private String contractType;
    private String salary;
    private String contractEndDate;
}
