package com.github.bartoszpop.jpa.specification;

import javax.persistence.criteria.Expression;
import java.util.Collection;

/**
 * This class provides predefined specifications applicable to instances of {@link Expression}.
 *
 * @author Bartosz Popiela
 */
public final class ExpressionSpecifications {
    private ExpressionSpecifications() {
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>> CompositeSpecification<T, S> equal(T object) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((target, query, criteriaBuilder) -> criteriaBuilder.equal(target, object));
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>> CompositeSpecification<T, S> greaterThan(T object) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((target, query, criteriaBuilder) -> criteriaBuilder.greaterThan(target, object));
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>> CompositeSpecification<T, S> greaterThanOrEqualTo(T object) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((target, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(target, object));
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>> CompositeSpecification<T, S> lessThan(T object) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((target, query, criteriaBuilder) -> criteriaBuilder.lessThan(target, object));
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>> CompositeSpecification<T, S> lessThanOrEqualTo(T object) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((target, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(target, object));
    }

    public static <T, S extends Expression<T>> CompositeSpecification<T, S> in(Collection<T> objects) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((target, query, criteriaBuilder) -> target.in(objects));
    }
}
