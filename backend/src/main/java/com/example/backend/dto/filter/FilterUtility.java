package com.example.backend.dto.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.backend.entity.BaseEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilterUtility<T extends BaseEntity> {
    /**
     *
     */
    private static final String LESS_OPERATOR = "<";

    /**
     *
     */
    private static final String EQUAL_OPERATOR = "=";

    /**
     *
     */
    private static final String GREATER_OPERATOR = ">";

    /**
     *
     */
    private static final String DATE_DATA_TYPE = "date";

    /**
     *
     */
    private static final String INTEGER_DATA_TYPE = "integer";

    /**
     *
     */
    private static final String STRING_DATA_TYPE = "string";


    private final EntityManager entityManager;
    private final Class<T> entityClass;

    public List<T> filterEntities(FilterCriteria filterCriteria) {
        Map<String, Object> filters = filterCriteria.getFilters();
        Map<String, String> operators = filterCriteria.getOperators();
        Map<String, String> dataTypes = filterCriteria.getDataTypes();


        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
       
        List<Predicate> predicates = new ArrayList<>();

        for(Map.Entry<String, Object> entry : filters.entrySet()) {
            String fieldName = entry.getKey();
            Object filterValue = entry.getValue();
            String operator = operators.get(fieldName);
            String dataType = dataTypes.get(fieldName);

            String[] nestedProperties = fieldName.split("\\.");

            Path<?> fieldPath = root;
            for (String nestedProperty : nestedProperties) {
                fieldPath = fieldPath.get(nestedProperty);
            }

            Predicate predicate = null;

            switch(operator) {
                case GREATER_OPERATOR:
                    predicate = buildGreaterThanPredicate(builder, fieldPath, dataType, filterValue);   
                    break;

                case EQUAL_OPERATOR:
                    predicate = buildEqualPredicate(builder, fieldPath, dataType, filterValue);
                    break;

                case LESS_OPERATOR:
                    predicate = buildLessThanPredicate(builder, fieldPath, dataType, filterValue);
                    break;

                default:
                    // Handle unsupported operators
                    break;
            }

            predicates.add(predicate);
            
        }

        query.where(predicates.toArray(new Predicate[0]));

        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    private Predicate buildGreaterThanPredicate(CriteriaBuilder builder, Path<?> fieldPath, String dataType, Object filterValue) {
        switch (dataType) {
            case STRING_DATA_TYPE:
                return builder.greaterThan(fieldPath.as(String.class), filterValue.toString());
            case INTEGER_DATA_TYPE:
                return builder.greaterThan(fieldPath.as(Integer.class), Integer.parseInt(filterValue.toString()));
            case DATE_DATA_TYPE:
                return builder.greaterThan(fieldPath.as(LocalDateTime.class), LocalDateTime.parse(filterValue.toString()));
            default:
                // Handle unsupported data types or provide an error response
                return null;
        }
    }

    private Predicate buildEqualPredicate(CriteriaBuilder builder, Path<?> fieldPath, String dataType, Object filterValue) {
        switch (dataType) {
            case STRING_DATA_TYPE:
                return builder.equal(fieldPath.as(String.class), filterValue.toString());
            case INTEGER_DATA_TYPE:
                return builder.equal(fieldPath.as(Integer.class), Integer.parseInt(filterValue.toString()));
            case DATE_DATA_TYPE:
                return builder.equal(fieldPath.as(LocalDateTime.class), LocalDate.parse(filterValue.toString()).atStartOfDay());
            default:
                // Handle unsupported data types or provide an error response
                return null;
        }
    }

    private Predicate buildLessThanPredicate(CriteriaBuilder builder, Path<?> fieldPath, String dataType, Object filterValue) {
        switch (dataType) {
            case STRING_DATA_TYPE:
                return builder.lessThan(fieldPath.as(String.class), filterValue.toString());
            case INTEGER_DATA_TYPE:
                return builder.lessThan(fieldPath.as(Integer.class), Integer.parseInt(filterValue.toString()));
            case DATE_DATA_TYPE:
                return builder.lessThan(fieldPath.as(LocalDateTime.class), LocalDateTime.parse(filterValue.toString()));
            default:
                // Handle unsupported data types or provide an error response
                return null;
        }
    }
}
