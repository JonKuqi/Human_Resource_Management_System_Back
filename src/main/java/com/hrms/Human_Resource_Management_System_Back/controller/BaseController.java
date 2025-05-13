package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract base controller class for handling CRUD operations for entities.
 * <p>
 * This class defines generic methods for interacting with entities in the database, including:
 * - Retrieving all entities
 * - Retrieving a specific entity by ID
 * - Creating a new entity
 * - Updating an existing entity
 * - Deleting an entity
 * - Filtering entities based on dynamic parameters
 * </p>
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's ID
 */
public abstract class BaseController<T, ID> {

    /**
     * Returns the service instance responsible for handling business logic for the entity.
     * @return the service for the specified entity
     */
    protected abstract BaseService<T, ID> getService();

    /**
     * Retrieves all entities of type {@code T}.
     * <p>
     * Fetches all records from the database and returns them as a list.
     * </p>
     *
     * @return a list of all entities
     */
    @Operation(
            summary = "Retrieve all entities",
            description = "Fetches all records of the specified entity type from the database."
    )
    @GetMapping
    public List<T> getAll() {
        return getService().findAll();
    }

    /**
     * Retrieves an entity by its ID.
     * <p>
     * Returns the entity with the specified ID if it exists; otherwise, returns a 404 Not Found.
     * </p>
     *
     * @param id the ID of the entity to retrieve
     * @return the entity with the specified ID, or 404 if not found
     */
    @Operation(
            summary = "Retrieve an entity by ID",
            description = "Returns the entity with the given ID if it exists, otherwise returns 404."
    )
    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) {
        Optional<T> entity = getService().findById(id);
        return entity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new entity.
     * <p>
     * Accepts a request body representing the entity and persists it to the database.
     * </p>
     *
     * @param entity the entity to create
     * @return the created entity
     */
    @Operation(
            summary = "Create a new entity",
            description = "Creates and persists a new entity in the system."
    )
    @PostMapping
    public T create(@RequestBody T entity) {
        return getService().save(entity);
    }

    /**
     * Updates an existing entity.
     * <p>
     * Accepts an ID and an entity. If the entity exists, it is updated with the provided data.
     * </p>
     *
     * @param id     the ID of the entity to update
     * @param entity the new data for the entity
     * @return the updated entity, or 404 if not found
     */
    @Operation(
            summary = "Update an existing entity",
            description = "Updates the entity identified by the given ID with the new data provided."
    )
    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
        // In a real application, ensure that entity.id equals id or merge as needed.
        Optional<T> existing = getService().findById(id);
        if (existing.isPresent()) {
            return ResponseEntity.ok(getService().save(entity));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an entity by its ID.
     * <p>
     * Deletes the entity with the specified ID from the system.
     * </p>
     *
     * @param id the ID of the entity to delete
     * @return a 204 No Content response if the deletion was successful
     */
    @Operation(
            summary = "Delete an entity",
            description = "Deletes the entity with the specified ID from the system."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        getService().deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Filters entities based on dynamic parameters.
     * <p>
     * Accepts a list of query parameters to filter entities, such as `/users/filter?email=example@...&username=user123`.
     * </p>
     *
     * @param filters a map of query parameters to filter the entities
     * @return a list of entities that match the filter criteria
     */
    @Operation(
            summary = "Filters an entity",
            description = "Filters entities based on dynamic parameters such as /users/filter?email=example@..&username=user123."
    )
    @GetMapping("/filter")
    public ResponseEntity<List<T>> filter(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(getService().findAllByFilter(filters));
    }
}