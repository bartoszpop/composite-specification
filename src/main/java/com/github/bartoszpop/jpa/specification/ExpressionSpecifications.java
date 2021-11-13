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

    public static <T extends Comparable<? super T>> CompositeSpecification<T, Expression<T>> equal(T object) {
        return CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root, object)
        );
    }

    public static <T extends Comparable<? super T>> CompositeSpecification<T, Expression<T>> greaterThan(T object) {
        return CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root, object)
        );
    }

    public static <T extends Comparable<? super T>> CompositeSpecification<T, Expression<T>> greaterThanOrEqualTo(T object) {
        return CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root, object)
        );
    }

    public static <T extends Comparable<? super T>> CompositeSpecification<T, Expression<T>> lessThan(T object) {
        return CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root, object)
        );
    }

    public static <T extends Comparable<? super T>> CompositeSpecification<T, Expression<T>> lessThanOrEqualTo(T object) {
        return CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root, object)
        );
    }

    public static <T> CompositeSpecification<T, Expression<T>> in(Collection<T> objects) {
        return CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> root.in(objects)
        );
    }
}
