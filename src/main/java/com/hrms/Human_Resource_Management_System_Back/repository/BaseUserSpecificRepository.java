package com.hrms.Human_Resource_Management_System_Back.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Base repository interface for user-specific entities with role-based access control (RBAC).
 * <p>
 * This interface extends {@link BaseRepository} and provides methods for querying and modifying entities based on user roles.
 * It ensures that only users with **every** role in the specified list can access or modify the associated entities.
 * </p>
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's ID
 */
@NoRepositoryBean
public interface BaseUserSpecificRepository<T, ID> extends BaseRepository<T, ID> {

    /**
     * Retrieves all entities where the user has **every** role specified in the provided list.
     * <p>
     * This method performs a role-based query, ensuring that the user associated with each entity owns all the roles
     * in the provided list.
     * </p>
     *
     * @param roles the list of roles that the user must own
     * @return a list of entities whose associated users have all the roles in the provided list
     */
    List<T> findAllRole(List<String> roles);

    /**
     * Retrieves an entity by its ID, but only if the user has **every** role specified in the provided list.
     * <p>
     * This method ensures that the entity can only be found if the associated user owns all the specified roles.
     * </p>
     *
     * @param id    the ID of the entity to retrieve
     * @param roles the list of roles that the user must own to access the entity
     * @return an {@link Optional} containing the entity if found and authorized, otherwise empty
     */
    Optional<T> findByIdRole(ID id, List<String> roles);

    /**
     * Deletes an entity by its ID, but only if the caller holds **every** role specified in the provided list.
     * <p>
     * This method ensures that the deletion only occurs if the caller (user) holds all the roles in the provided list.
     * It uses the {@link Modifying} annotation for an update operation and {@link Transactional} to ensure atomicity.
     * </p>
     *
     * @param id    the ID of the entity to delete
     * @param roles the list of roles that the caller must own to delete the entity
     */
    @Modifying
    @Transactional
    void deleteByIdRole(ID id, List<String> roles);
}