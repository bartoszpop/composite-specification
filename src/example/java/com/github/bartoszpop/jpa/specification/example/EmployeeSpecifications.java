package com.github.bartoszpop.jpa.specification.example;

import com.github.bartoszpop.jpa.specification.CompositeSpecification;
import com.github.bartoszpop.jpa.specification.ExpressionSpecifications;
import com.github.bartoszpop.jpa.specification.TypeSafePredicateBuilder;

import javax.persistence.criteria.Path;
import java.time.LocalDate;

public final class EmployeeSpecifications {

    private EmployeeSpecifications() {
    }

    public static <S extends Path<Employee>> CompositeSpecification<Employee, S> firstName(String firstName) {
        return CompositeSpecification.<Employee, S, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstName"), firstName)
        );
    }

    public static <S extends Path<Employee>> CompositeSpecification<Employee, S> secondName(String secondName) {
        return CompositeSpecification.<Employee, S, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("secondName"), secondName)
        );
    }

    public static <S extends Path<Employee>> CompositeSpecification<Employee, S> dateOfBirth(LocalDate dateOfBirth) {
        return dateOfBirth(ExpressionSpecifications.equal(dateOfBirth));
    }

    public static <S extends Path<Employee>> CompositeSpecification<Employee, S> dateOfBirth(CompositeSpecification<?, ? super Path<LocalDate>> dateOfBirthSpecification) {
        return CompositeSpecification.<Employee, S, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> dateOfBirthSpecification.asBuilder().toPredicate(root.get("dateOfBirth"), query, criteriaBuilder)
        );
    }
}
