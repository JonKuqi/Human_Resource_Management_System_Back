package com.hrms.Human_Resource_Management_System_Back.repository;

import com.hrms.Human_Resource_Management_System_Back.model.Address;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Address} entities.
 * <p>
 * Extends {@link BaseRepository} to provide standard CRUD operations
 * for address data in the system.
 * </p>
 */
@Repository
public interface AddressRepository extends BaseRepository<Address, Integer>{
}
