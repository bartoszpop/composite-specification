package com.github.bartoszpop.jpa.specification.example;

import com.github.bartoszpop.jpa.specification.CompositeSpecification;
import com.github.bartoszpop.jpa.specification.ExpressionSpecifications;
import com.github.bartoszpop.jpa.specification.TypeSafePredicateBuilder;

import javax.persistence.criteria.Path;
import java.time.LocalDate;

public final class EmployeeSpecifications {

    private EmployeeSpecifications() {
    }

    public static CompositeSpecification<Employee, Path<Employee>> firstName(String firstName) {
        return CompositeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstName"), firstName)
        );
    }

    public static CompositeSpecification<Employee, Path<Employee>> secondName(String secondName) {
        return CompositeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("secondName"), secondName)
        );
    }

    public static CompositeSpecification<Employee, Path<Employee>> dateOfBirth(LocalDate dateOfBirth) {
        return dateOfBirth(ExpressionSpecifications.equal(dateOfBirth));
    }

    public static CompositeSpecification<Employee, Path<Employee>> dateOfBirth(CompositeSpecification<?, ? super Path<LocalDate>> dateOfBirthSpecification) {
        return CompositeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> dateOfBirthSpecification.asBuilder().toPredicate(root.get("dateOfBirth"), query, criteriaBuilder)
        );
    }
}
