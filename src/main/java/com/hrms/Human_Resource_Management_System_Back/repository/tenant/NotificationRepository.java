package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Notification;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Notification} entities.
 * <p>
 * This interface extends {@link BaseRepository} to provide standard CRUD operations
 * and defines a custom method for bulk deletion of notifications based on the associated user.
 * </p>
 */
@Repository
public interface NotificationRepository extends BaseRepository<Notification, Integer> {

    /**
     * Deletes all notifications associated with a specific user tenant.
     *
     * @param testUserTenant the {@link UserTenant} whose notifications should be deleted
     */
    void deleteAllByUserTenant(UserTenant testUserTenant);
}
