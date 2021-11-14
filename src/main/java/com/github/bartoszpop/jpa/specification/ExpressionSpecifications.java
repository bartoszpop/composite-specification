package com.github.bartoszpop.jpa.specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;

/**
 * This class provides predefined specifications applicable to instances of {@link Expression}.
 *
 * @author Bartosz Popiela
 */
public final class ExpressionSpecifications {
    private ExpressionSpecifications() {
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>, U extends PredicateBuilder<S> & TypeSafe<? super Root<T>>> CompositeSpecification<T, S> equal(T object) {
        // Cast allowed because CompositeSpecification<T, Expression<T>> consumes instances of S
        //noinspection unchecked
        return (CompositeSpecification<T,S>) CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root, object)
        );
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>, U extends PredicateBuilder<S> & TypeSafe<? super Root<T>>> CompositeSpecification<T, S> greaterThan(T object) {
        // Cast allowed because CompositeSpecification<T, Expression<T>> is only a consumer of S
        //noinspection unchecked
        return (CompositeSpecification<T,S>) CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root, object)
        );
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>, U extends PredicateBuilder<S> & TypeSafe<? super Root<T>>> CompositeSpecification<T, S> greaterThanOrEqualTo(T object) {
        // Cast allowed because CompositeSpecification<T, Expression<T>> is only a consumer of S
        //noinspection unchecked
        return (CompositeSpecification<T,S>) CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root, object)
        );
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>, U extends PredicateBuilder<S> & TypeSafe<? super Root<T>>> CompositeSpecification<T, S> lessThan(T object) {
        // Cast allowed because CompositeSpecification<T, Expression<T>> is only a consumer of S
        //noinspection unchecked
        return (CompositeSpecification<T,S>) CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root, object)
        );
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>, U extends PredicateBuilder<? super S> & TypeSafe<? super Root<T>>> CompositeSpecification<T, S> lessThanOrEqualTo(T object) {
        // Cast allowed because CompositeSpecification<T, Expression<T>> is only a consumer of S
        //noinspection unchecked
        return (CompositeSpecification<T,S>) CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root, object)
        );
    }

    public static <T, S extends Expression<T>, U extends PredicateBuilder<S> & TypeSafe<? super Root<T>>> CompositeSpecification<T, S> in(Collection<T> objects) {
        // Cast allowed because CompositeSpecification<T, Expression<T>> is only a consumer of S
        //noinspection unchecked
        return (CompositeSpecification<T,S>) CompositeSpecification.<T,Expression<T>,TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> root.in(objects)
        );
    }
}
