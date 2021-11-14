package com.github.bartoszpop.jpa.specification.example;

import com.github.bartoszpop.jpa.specification.CompositeSpecification;
import com.github.bartoszpop.jpa.specification.PredicateBuilder;
import com.github.bartoszpop.jpa.specification.TypeSafe;
import com.github.bartoszpop.jpa.specification.TypeSafePredicateBuilder;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import static com.github.bartoszpop.jpa.specification.CompositeSpecification.noOp;

public final class DepartmentSpecifications {

    private DepartmentSpecifications() {
    }

    public static <T extends Path<Department>, S extends PredicateBuilder<T> & TypeSafe<? super Root<Department>>> CompositeSpecification<Department, T> name(String name) {
        // Cast allowed because CompositeSpecification<Department, Path<Department>> is only a consumer of T
        //noinspection unchecked
        return (CompositeSpecification<Department, T>) CompositeSpecification.<Department, Path<Department>, TypeSafePredicateBuilder<Path<Department>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name)
        );
    }

    public static <T extends From<?, Department>, S extends PredicateBuilder<T> & TypeSafe<? super Root<Department>>> CompositeSpecification<Department, T> fetchEmployees() {
        return fetchEmployees(noOp());
    }

    public static <T extends From<?, Department>, S extends PredicateBuilder<T> & TypeSafe<? super Root<Department>>> CompositeSpecification<Department, T>  fetchEmployees(CompositeSpecification<?, ? super Join<?, Employee>> employeeSpecification) {
        // Cast allowed because CompositeSpecification<Department, From<?, Department>> is only a consumer of T
        //noinspection unchecked
        return (CompositeSpecification<Department, T>) CompositeSpecification.<Department, From<?, Department>, TypeSafePredicateBuilder<From<?, Department>>>of(
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

    public static <T extends From<?, Department>, S extends PredicateBuilder<T> & TypeSafe<? super Root<Department>>> CompositeSpecification<Department, T> joinEmployees(CompositeSpecification<?, ? super Join<?, Employee>> employeeSpecification) {
        // Cast allowed because CompositeSpecification<Department, From<?, Department>> is only a consumer of T
        //noinspection unchecked
        return (CompositeSpecification<Department, T>) CompositeSpecification.<Department, From<?, Department>, TypeSafePredicateBuilder<From<?, Department>>>of(
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
