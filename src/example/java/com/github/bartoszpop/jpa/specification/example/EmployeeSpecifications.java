package com.github.bartoszpop.jpa.specification.example;

import com.github.bartoszpop.jpa.specification.CompositeSpecification;
import com.github.bartoszpop.jpa.specification.ExpressionSpecifications;
import com.github.bartoszpop.jpa.specification.PredicateBuilder;
import com.github.bartoszpop.jpa.specification.TypeSafe;
import com.github.bartoszpop.jpa.specification.TypeSafePredicateBuilder;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.time.LocalDate;

public final class EmployeeSpecifications {

    private EmployeeSpecifications() {
    }

    public static <T extends Path<Employee>, S extends PredicateBuilder<T> & TypeSafe<? super Root<Employee>>> CompositeSpecification<Employee, T> firstName(String firstName) {
        // Cast allowed because CompositeSpecification<Employee, Path<Employee>> is only a consumer of T
        //noinspection unchecked
        return (CompositeSpecification<Employee, T>) CompositeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstName"), firstName)
        );
    }

    public static <T extends Path<Employee>, S extends PredicateBuilder<T> & TypeSafe<? super Root<Employee>>> CompositeSpecification<Employee, T> secondName(String secondName) {
        // Cast allowed because CompositeSpecification<Employee, Path<Employee>> is only a consumer of T
        //noinspection unchecked
        return (CompositeSpecification<Employee, T>) CompositeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("secondName"), secondName)
        );
    }

    public static <T extends Path<Employee>, S extends PredicateBuilder<T> & TypeSafe<? super Root<Employee>>> CompositeSpecification<Employee, T> dateOfBirth(LocalDate dateOfBirth) {
        return dateOfBirth(ExpressionSpecifications.equal(dateOfBirth));
    }

    public static <T extends Path<Employee>, S extends PredicateBuilder<T> & TypeSafe<? super Root<Employee>>> CompositeSpecification<Employee, T> dateOfBirth(CompositeSpecification<?, ? super Path<LocalDate>> dateOfBirthSpecification) {
        // Cast allowed because CompositeSpecification<Employee, Path<Employee>> is only a consumer of T
        //noinspection unchecked
        return (CompositeSpecification<Employee, T>) CompositeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> dateOfBirthSpecification.asBuilder().toPredicate(root.get("dateOfBirth"), query, criteriaBuilder)
        );
    }
}
