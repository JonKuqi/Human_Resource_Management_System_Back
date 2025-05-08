package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.BaseUserSpecificService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public abstract class BaseUserSpecificController<T,ID>
        extends BaseController<T,ID> {

    protected abstract BaseUserSpecificService<T, ID> getServiceSpecific();

    @Override
    protected BaseService<T, ID> getService() {
        return getServiceSpecific();
    }

    /* pulls the list once; downstream code reuses it */
    @SuppressWarnings("unchecked")
    private List<String> roles(HttpServletRequest req) {
        Object o = req.getAttribute("target_roles");
        return o == null ? List.of() : (List<String>) o;
    }

    /* --- endpoints every role-aware resource gets --- */

    @GetMapping("/role-based/")
    public List<T> listRole(HttpServletRequest req) {
        return getServiceSpecific().findAllRole(roles(req));
    }

    @GetMapping("/role-based/{id}")
    public ResponseEntity<T> getByIdRole(@PathVariable ID id,
                                         HttpServletRequest req) {
        return getServiceSpecific().findByIdRole(id, roles(req))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN));
    }

    @DeleteMapping("/role-based/{id}")
    public void deleteByIdRole(@PathVariable ID id,
                               HttpServletRequest req) {
        getServiceSpecific().deleteByIdRole(id, roles(req));
    }
}