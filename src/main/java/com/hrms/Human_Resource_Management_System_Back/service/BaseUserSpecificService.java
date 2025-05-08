package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;

import java.util.List;
import java.util.Optional;

public abstract class BaseUserSpecificService<T, ID> extends BaseService<T, ID>{
    protected abstract BaseUserSpecificRepository<T, ID> getRepository();

    public List<T> findAllRole(List<String> roles) {
       // Filters users who have ALL THE ROLES.
        return getRepository().findAllRole(roles);
    }

    public Optional<T> findByIdRole(ID id, List<String> roles) {
        //Able to find only if it has the role, if not NOT ALLOWED
        return getRepository().findByIdRole(id, roles);
    }


    public void deleteByIdRole(ID id, List<String> roles) {
        getRepository().deleteByIdRole(id, roles);
        //Delete only if the user that HAVE ALL ROLES if else NOT ALLOWED
    }


}
