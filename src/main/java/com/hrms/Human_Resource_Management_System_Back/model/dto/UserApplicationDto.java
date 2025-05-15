package com.hrms.Human_Resource_Management_System_Back.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserApplicationDto {

    Integer userID;
    Integer jobListingID;
    String applicantComment;
}
