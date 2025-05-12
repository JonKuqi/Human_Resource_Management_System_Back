package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseController<T, ID> {
    protected abstract BaseService<T, ID> getService();

    @Operation(
            summary = "Retrieve all entities",
            description = "Fetches all records of the specified entity type from the database."
    )
    @GetMapping
    public List<T> getAll() {
        return getService().findAll();
    }


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

    @Operation(
            summary = "Create a new entity",
            description = "Creates and persists a new entity in the system."
    )
    @PostMapping
    public T create(@RequestBody T entity) {
        return getService().save(entity);
    }

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

    @Operation(
            summary = "Delete an entity",
            description = "Deletes the entity with the specified ID from the system."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        getService().deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Filters an entity ",
            description = "Deletes the entity based on columns example /users/filter?email=example@..&username=user123"
    )
    @GetMapping("/filter")
    public ResponseEntity<List<T>> filter(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(getService().findAllByFilter(filters));
    }

}