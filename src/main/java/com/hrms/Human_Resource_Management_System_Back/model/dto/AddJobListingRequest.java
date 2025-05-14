package com.hrms.Human_Resource_Management_System_Back.model.dto;


import com.hrms.Human_Resource_Management_System_Back.model.Industry;
import com.hrms.Human_Resource_Management_System_Back.model.types.EmploymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddJobListingRequest {

    private String jobTitle;
    private Industry industry;
    private String customIndustry;
    private String location;
    private EmploymentType employmentType;
    private String description;
    private String aboutUs;
    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    private LocalDate validUntil;
    private List<String> tags;

}
