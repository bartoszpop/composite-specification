package com.github.bartoszpop.jpa.specification;

/**
 * This is the only {@link PredicateBuilder} directly derived from {@link TypeSafe}.
 * Only instances of this class may be passed to {@link CompositeSpecification#of}.
 *
 * @author Bartosz Popiela
 */
public non-sealed interface TypeSafePredicateBuilder<T> extends PredicateBuilder<T>, TypeSafe<T> {
}
