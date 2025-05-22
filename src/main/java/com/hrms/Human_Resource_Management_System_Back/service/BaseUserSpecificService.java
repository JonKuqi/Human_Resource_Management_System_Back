package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;

import java.util.List;
import java.util.Optional;

/**
 * Abstract service class for handling business logic related to entities that are user-specific.
 * <p>
 * This class extends {@link BaseService} and provides methods for filtering and performing actions based on
 * roles assigned to users. It interacts with a repository that supports role-based access control (RBAC).
 * Subclasses must define the specific repository for the entity type {@code T}.
 * </p>
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's ID
 */
public abstract class BaseUserSpecificService<T, ID> extends BaseService<T, ID> {

    /**
     * Returns the repository for the entity type {@code T}.
     * <p>
     * Subclasses should override this method to return the specific repository for the user-specific entity.
     * </p>
     *
     * @return the repository for the entity
     */
    protected abstract BaseUserSpecificRepository<T, ID> getRepository();

    /**
     * Retrieves all entities associated with the specified roles.
     * <p>
     * This method fetches all records from the repository where the user has all of the provided roles.
     * </p>
     *
     * @param roles the list of roles that the users must have
     * @return a list of entities that have all the specified roles
     */
    public List<T> findAllRole(List<String> roles) {
        // Filters users who have ALL THE ROLES.
        return getRepository().findAllRole(roles);
    }

    /**
     * Retrieves an entity by its ID, but only if the user has the required roles.
     * <p>
     * This method checks if the user associated with the entity has all of the specified roles.
     * If the user doesn't have the required roles, the entity is not returned.
     * </p>
     *
     * @param id    the ID of the entity to retrieve
     * @param roles the list of roles that the user must have to access the entity
     * @return an {@link Optional} containing the entity if found and authorized, otherwise empty
     */
    public Optional<T> findByIdRole(ID id, List<String> roles) {
        // Able to find only if the user has ALL THE ROLES, if not, NOT ALLOWED.
        return getRepository().findByIdRole(id, roles);
    }

    /**
     * Deletes an entity by its ID, but only if the user has the required roles.
     * <p>
     * This method checks if the user associated with the entity has all of the specified roles.
     * If the user doesn't have the required roles, the deletion is not performed.
     * </p>
     *
     * @param id    the ID of the entity to delete
     * @param roles the list of roles that the user must have to delete the entity
     */
    public void deleteByIdRole(ID id, List<String> roles) {
        // Delete only if the user has ALL THE ROLES, if not, NOT ALLOWED.
        getRepository().deleteByIdRole(id, roles);
    }
}
