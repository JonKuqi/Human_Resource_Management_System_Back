package com.hrms.Human_Resource_Management_System_Back.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseUserSpecificRepository<T, ID> extends BaseRepository<T, ID> {

    /** Return rows whose tenant-user owns **every** role in roles  */
    List<T> findAllRole(List<String> roles);

    /** Same rule, but for a single primary-key lookup              */
    Optional<T> findByIdRole(ID id, List<String> roles);

    /** Delete the row only when caller holds **every** role        */
    @Modifying
    @Transactional
    void deleteByIdRole(ID id, List<String> roles);
}