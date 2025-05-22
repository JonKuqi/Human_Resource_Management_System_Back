package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.filter.FilterSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract service class providing common CRUD operations for entities of type {@code T}.
 * <p>
 * This class provides basic methods for interacting with the repository, including finding all entities,
 * finding an entity by ID, saving an entity, deleting an entity by ID, and filtering entities by various criteria.
 * Subclasses must define the specific repository for the entity type {@code T}.
 * </p>
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's ID
 */
public abstract class BaseService<T, ID> {

    /**
     * Returns the repository for the entity type {@code T}.
     * <p>
     * Subclasses should override this method to return the specific repository for the entity.
     * </p>
     *
     * @return the repository for the entity
     */
    protected abstract BaseRepository<T, ID> getRepository();

    /**
     * Retrieves all entities of type {@code T}.
     * <p>
     * This method fetches all records from the underlying repository and returns them as a list.
     * </p>
     *
     * @return a list of all entities
     */
    public List<T> findAll() {
        return getRepository().findAll();
    }

    /**
     * Retrieves an entity by its ID.
     * <p>
     * This method returns the entity with the specified ID if it exists, or an empty {@link Optional} if not found.
     * </p>
     *
     * @param id the ID of the entity to retrieve
     * @return an {@link Optional} containing the entity if found, or empty if not found
     */
    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    /**
     * Saves a new or existing entity to the repository.
     * <p>
     * This method persists the given entity to the database. If the entity is new, it will be inserted;
     * if it already exists, it will be updated.
     * </p>
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    public T save(T entity) {
        return getRepository().save(entity);
    }

    /**
     * Deletes an entity by its ID.
     * <p>
     * This method deletes the entity with the specified ID from the repository.
     * </p>
     *
     * @param id the ID of the entity to delete
     */
    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    /**
     * Filters entities based on the provided criteria.
     * <p>
     * This method creates a {@link Specification} based on the provided filter criteria and fetches the matching entities
     * from the repository.
     * </p>
     *
     * @param filters a map of filter criteria where the key is the field name and the value is the filter value
     * @return a list of entities that match the filter criteria
     */
    public List<T> findAllByFilter(Map<String, String> filters) {
        Specification<T> spec = new FilterSpecification<>(filters);
        return getRepository().findAll(spec);
    }
}