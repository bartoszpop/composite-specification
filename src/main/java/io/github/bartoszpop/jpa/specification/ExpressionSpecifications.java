package io.github.bartoszpop.jpa.specification;

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
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((root, query, criteriaBuilder) -> criteriaBuilder.equal(root, object));
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>> CompositeSpecification<T, S> greaterThan(T object) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root, object));
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>> CompositeSpecification<T, S> greaterThanOrEqualTo(T object) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root, object));
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>> CompositeSpecification<T, S> lessThan(T object) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root, object));
    }

    public static <T extends Comparable<? super T>, S extends Expression<T>> CompositeSpecification<T, S> lessThanOrEqualTo(T object) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root, object));
    }

    public static <T, S extends Expression<T>> CompositeSpecification<T, S> in(Collection<T> objects) {
        return CompositeSpecification.<T, S, TypeSafePredicateBuilder<Expression<T>>>of((root, query, criteriaBuilder) -> root.in(objects));
    }
}
