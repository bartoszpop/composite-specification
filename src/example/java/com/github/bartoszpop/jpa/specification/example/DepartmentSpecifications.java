package com.github.bartoszpop.jpa.specification.example;

import com.github.bartoszpop.jpa.specification.CompositeSpecification;
import com.github.bartoszpop.jpa.specification.TypeSafePredicateBuilder;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;

import static com.github.bartoszpop.jpa.specification.CompositeSpecification.noOp;

public final class DepartmentSpecifications {

    private DepartmentSpecifications() {
    }

    public static CompositeSpecification<Department, Path<Department>> name(String name) {
        return CompositeSpecification.<Department, Path<Department>, TypeSafePredicateBuilder<Path<Department>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name)
        );
    }

    public static CompositeSpecification<Department, From<?, Department>> fetchEmployees() {
        return fetchEmployees(noOp());
    }

    public static CompositeSpecification<Department, From<?, Department>> fetchEmployees(CompositeSpecification<?, ? super Join<?, Employee>> employeeSpecification) {
        return CompositeSpecification.<Department, From<?, Department>, TypeSafePredicateBuilder<From<?, Department>>>of(
                (root, query, criteriaBuilder) -> {
                    /*
                    This is to return distinct Department instances. If two employees are in the same department, this query will return two instances of the same Department.
                    See https://vladmihalcea.com/eager-fetching-is-a-code-smell for details.
                     */
                    query.distinct(true);

                    // Cast allowed because Hibernate and EclipseLink return an instance of Join as per https://thorben-janssen.com/hibernate-tip-left-join-fetch-join-criteriaquery
                    //noinspection unchecked
                    return employeeSpecification.asBuilder().toPredicate((Join<Department, Employee>) root.<Department, Employee>fetch("employees", JoinType.LEFT), query, criteriaBuilder);
                });
    }

    public static CompositeSpecification<Department, From<?, Department>> joinEmployees(CompositeSpecification<?, ? super Join<?, Employee>> employeeSpecification) {
        return CompositeSpecification.<Department, From<?, Department>, TypeSafePredicateBuilder<From<?, Department>>>of(
                (root, query, criteriaBuilder) -> {
                    /*
                    This is to return distinct Department instances. If two employees are in the same department, this query will return two instances of the same Department.
                    See https://vladmihalcea.com/eager-fetching-is-a-code-smell for details.
                     */
                    query.distinct(true);

                    return employeeSpecification.asBuilder().toPredicate(root.<Department, Employee>join("employees", JoinType.LEFT), query, criteriaBuilder);
                });
    }
}
