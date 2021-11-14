package io.github.bartoszpop.jpa.specification.example;

import io.github.bartoszpop.jpa.specification.CompositeSpecification;
import io.github.bartoszpop.jpa.specification.TypeSafePredicateBuilder;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;

import static io.github.bartoszpop.jpa.specification.CompositeSpecification.noOp;

public final class DepartmentSpecifications {

    private DepartmentSpecifications() {
    }

    public static <S extends Path<Department>> CompositeSpecification<Department, S> name(String name) {
        return CompositeSpecification.<Department, S, TypeSafePredicateBuilder<Path<Department>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name)
        );
    }

    public static <S extends From<?, Department>> CompositeSpecification<Department, S> fetchEmployees() {
        return fetchEmployees(noOp());
    }

    public static <S extends From<?, Department>> CompositeSpecification<Department, S> fetchEmployees(CompositeSpecification<?, ? super Join<?, Employee>> employeeSpecification) {
        return CompositeSpecification.<Department, S, TypeSafePredicateBuilder<From<?, Department>>>of(
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

    public static <S extends From<?, Department>> CompositeSpecification<Department, S> joinEmployees(CompositeSpecification<?, ? super Join<?, Employee>> employeeSpecification) {
        return CompositeSpecification.<Department, S, TypeSafePredicateBuilder<From<?, Department>>>of(
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
