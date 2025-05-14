package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Application;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.ApplicationRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.EmailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ApplicationService extends BaseService<Application, Integer> {
    private final ApplicationRepository repo;


    @Override
    protected ApplicationRepository getRepository() {
        return repo;
    }


    @Override
    public Application save(Application application) {
        if (application.getApplicationId() != null) {
            Application existing = getRepository().findById(application.getApplicationId())
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            // Check if status changed
            if (!existing.getStatus().equals(application.getStatus())) {
                EmailSenderService.sendApplicationStatusEmail(
                        application.getApplicantEmail(),
                        application.getApplicantName(),
                        existing.getJobListing().getJobTitle(),
                        application.getStatus()
                );
            }
        }

        application.setUpdatedAt(LocalDateTime.now());

        return getRepository().save(application);
    }




}
