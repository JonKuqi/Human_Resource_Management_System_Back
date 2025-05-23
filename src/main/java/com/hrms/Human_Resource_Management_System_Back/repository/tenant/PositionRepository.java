package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Position;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for managing {@link Position} entities.
 * <p>
 * Extends {@link BaseRepository} to provide standard CRUD operations and
 * includes a custom query method to find a position by its title.
 * </p>
 */
@Repository
public interface PositionRepository extends BaseRepository<Position, Integer> {
    /**
     * Finds a position entity by its title.
     *
     * @param title the title of the position to find
     * @return an {@link Optional} containing the matching {@link Position}, if found
     */
    Optional<Position> findByTitle(String title);
}
