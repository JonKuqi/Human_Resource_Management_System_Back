package com.hrms.Human_Resource_Management_System_Back.repository.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class FilterSpecification<T> implements Specification<T> {

    private final Map<String, String> filters;

    public FilterSpecification(Map<String, String> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            try {
                if (root.get(field) != null) {
                    predicate = cb.and(predicate, cb.equal(root.get(field), value));
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid filter field: " + field);
            }
        }

        return predicate;
    }
}