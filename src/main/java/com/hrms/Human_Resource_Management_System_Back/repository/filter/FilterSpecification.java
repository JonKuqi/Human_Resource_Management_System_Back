package com.hrms.Human_Resource_Management_System_Back.repository.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

/**
 * Custom {@link Specification} for filtering entities based on a map of field-value pairs.
 * <p>
 * This specification allows dynamic filtering of entities based on the provided filter criteria.
 * It takes a map of field names and their corresponding filter values, and constructs a {@link Predicate}
 * that can be used to query the database with the specified conditions.
 * </p>
 *
 * @param <T> the type of the entity being filtered
 */
public class FilterSpecification<T> implements Specification<T> {

    /**
     * The map of filters where the key is the field name and the value is the filter value.
     * <p>
     * This map contains the criteria for filtering the entities. For example, a filter could be
     * "firstName=John" where "firstName" is the field name and "John" is the value to match.
     * </p>
     */
    private final Map<String, String> filters;

    /**
     * Constructor for initializing the {@link FilterSpecification} with a map of filters.
     * <p>
     * This constructor sets up the filters that will be applied to the query. Each entry in the map
     * represents a condition for filtering entities by a specific field.
     * </p>
     *
     * @param filters the map of filters to apply to the entity fields
     */
    public FilterSpecification(Map<String, String> filters) {
        this.filters = filters;
    }

    /**
     * Converts the filter criteria into a {@link Predicate} for the query.
     * <p>
     * This method builds the filtering logic based on the provided filters. It iterates over the map of filters,
     * and for each filter, it adds a condition to the {@link Predicate}. If the filter field is invalid, it catches
     * the exception and logs an error message.
     * </p>
     *
     * @param root the root entity in the query, used to access entity attributes
     * @param query the query being constructed
     * @param cb the criteria builder, used to construct query expressions
     * @return the predicate that represents the filtering conditions for the query
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();

            try {
                // Support nested fields like role.id
                String[] parts = field.split("\\.");
                if (parts.length == 1) {
                    predicate = cb.and(predicate, cb.equal(root.get(parts[0]), value));
                } else if (parts.length == 2) {
                    predicate = cb.and(predicate, cb.equal(root.get(parts[0]).get(parts[1]), value));
                } else {
                    System.out.println("Invalid nested filter field: " + field);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid filter field: " + field);
            }
        }

        return predicate;
    }

}