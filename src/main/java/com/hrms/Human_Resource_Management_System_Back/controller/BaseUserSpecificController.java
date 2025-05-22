package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.BaseUserSpecificService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Abstract base controller for handling role-based access control.
 * <p>
 * This controller extends {@link BaseController} and provides role-based access to the specified entity.
 * It is designed to handle requests with role-based authorization, where access to the resources is determined
 * based on the roles associated with the user.
 * </p>
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's ID
 */
public abstract class BaseUserSpecificController<T, ID> extends BaseController<T, ID> {

    /**
     * Returns the service responsible for handling business logic for role-based entities.
     *
     * @return the service for role-based entities
     */
    protected abstract BaseUserSpecificService<T, ID> getServiceSpecific();

    /**
     * Overrides {@link BaseController#getService()} to return the service that handles role-specific entities.
     *
     * @return the role-specific service
     */
    @Override
    protected BaseService<T, ID> getService() {
        return getServiceSpecific();
    }

    /**
     * Retrieves the roles of the current user from the request.
     * <p>
     * This method pulls the roles from the HTTP request's attributes, which were set during authentication.
     * If no roles are found, it returns an empty list.
     * </p>
     *
     * @param req the {@link HttpServletRequest} containing the user roles
     * @return a list of roles for the current user
     */
    @SuppressWarnings("unchecked")
    private List<String> roles(HttpServletRequest req) {
        Object o = req.getAttribute("target_roles");
        return o == null ? List.of() : (List<String>) o;
    }

    /**
     * Lists the entities that the current user has access to based on their roles.
     * <p>
     * This endpoint uses the current user's roles to filter the list of entities returned from the service.
     * </p>
     *
     * @param req the {@link HttpServletRequest} containing the user roles
     * @return a list of entities the user is authorized to access
     */
    @GetMapping("/role-based/")
    public List<T> listRole(HttpServletRequest req) {
        return getServiceSpecific().findAllRole(roles(req));
    }

    /**
     * Retrieves a specific entity by ID if the current user has the appropriate role to access it.
     * <p>
     * This method checks if the user has the required roles to access the entity with the given ID.
     * If the user is not authorized, a {@link ResponseStatusException} with status {@link HttpStatus#FORBIDDEN} is thrown.
     * </p>
     *
     * @param id  the ID of the entity to retrieve
     * @param req the {@link HttpServletRequest} containing the user roles
     * @return the entity if the user is authorized, otherwise throws {@link ResponseStatusException}
     */
    @GetMapping("/role-based/{id}")
    public ResponseEntity<T> getByIdRole(@PathVariable ID id,
                                         HttpServletRequest req) {
        return getServiceSpecific().findByIdRole(id, roles(req))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN));
    }

    /**
     * Deletes a specific entity by ID if the current user has the appropriate role to perform the deletion.
     * <p>
     * This method checks if the user has the required roles to delete the entity with the given ID.
     * </p>
     *
     * @param id  the ID of the entity to delete
     * @param req the {@link HttpServletRequest} containing the user roles
     */
    @DeleteMapping("/role-based/{id}")
    public void deleteByIdRole(@PathVariable ID id,
                               HttpServletRequest req) {
        getServiceSpecific().deleteByIdRole(id, roles(req));
    }
}
