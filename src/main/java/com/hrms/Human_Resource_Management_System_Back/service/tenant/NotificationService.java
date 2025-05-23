package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Notification;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.NotificationRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for managing notification-related business logic.
 * <p>
 * This service provides operations for creating, updating, retrieving,
 * and deleting {@link Notification} entities. It extends {@link BaseService}
 * to inherit standard CRUD functionality and uses {@link NotificationRepository}
 * for data access.
 * </p>
 */
@Service
@AllArgsConstructor
public class NotificationService extends BaseService<Notification, Integer> {

    /**
     * The repository used for accessing notification data.
     */
    private final NotificationRepository repo;

    /**
     * Returns the repository instance for {@link Notification} entities.
     *
     * @return the {@link NotificationRepository} implementation
     */
    @Override
    protected NotificationRepository getRepository() {
        return repo;
    }
}
