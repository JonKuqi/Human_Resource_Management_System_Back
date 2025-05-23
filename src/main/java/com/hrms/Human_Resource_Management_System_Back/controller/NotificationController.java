package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Notification;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing notifications within a tenant.
 * <p>
 * This controller exposes REST endpoints for performing CRUD operations
 * on {@link Notification} entities. It extends {@link BaseController}
 * to reuse common functionality and delegates business logic to
 * {@link NotificationService}.
 * </p>
 * <p>
 * Endpoint path: <code>/api/v1/tenant/notification</code>
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/notification")
@AllArgsConstructor
public class NotificationController extends BaseController<Notification, Integer> {

    /**
     * The service responsible for handling notification-related operations.
     */
    private final NotificationService svc;

    /**
     * Provides the specific service implementation for notification logic.
     *
     * @return the {@link NotificationService} instance
     */
    @Override
    protected NotificationService getService() {
        return svc;
    }
}
