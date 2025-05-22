package com.hrms.Human_Resource_Management_System_Back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * Base repository interface that extends {@link JpaRepository} and {@link JpaSpecificationExecutor}.
 * <p>
 * This interface serves as a base for other repositories in the application, providing standard CRUD operations
 * as well as the ability to execute specifications (queries with dynamic filters).
 * It is not meant to be instantiated directly but used as a parent interface for more specific repositories.
 * </p>
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's ID
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
