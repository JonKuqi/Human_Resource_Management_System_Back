package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Notification;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.NotificationRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService extends BaseService<Notification, Integer> {
    private final NotificationRepository repo;

    @Override
    protected NotificationRepository getRepository() {
        return repo;
    }
}
