package com.hrms.Human_Resource_Management_System_Back.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data transfer object (DTO) for user job applications.
 * <p>
 * - userID: The ID of the user applying for the job.
 * - jobListingID: The ID of the job listing to which the user is applying.
 * - applicantComment: An optional comment provided by the applicant.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserApplicationDto {

    Integer userID;
    Integer jobListingID;
    String applicantComment;


}
