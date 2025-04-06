package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class BaseService<T, ID> {

    protected abstract BaseRepository<T, ID> getRepository();

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }
}
