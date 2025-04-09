package com.hrms.Human_Resource_Management_System_Back.controller.tenant;

import com.hrms.Human_Resource_Management_System_Back.controller.BaseController;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Notification;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
public class NotificationController extends BaseController<Notification, Integer> {
    private final NotificationService svc;

    @Override
    protected NotificationService getService() {
        return svc;
    }
}
