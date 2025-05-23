package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Industry;
import org.springframework.stereotype.Repository;
/**
 * Repository interface for managing {@link Industry} entities.
 * <p>
 * This repository extends {@link BaseRepository} and provides standard CRUD operations
 * for industries, which represent different sectors or domains in the system.
 * </p>
 */
@Repository
public interface IndustryRepository extends BaseRepository<Industry, Integer> {
}