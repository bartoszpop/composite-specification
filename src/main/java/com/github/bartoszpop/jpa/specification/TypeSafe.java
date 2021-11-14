package com.github.bartoszpop.jpa.specification;

/**
 * This class is a workaround for the lower-bound of {@link PredicateBuilder} in {@link CompositeSpecification#of} to be restricted.
 * It is sealed to ensure that only instances of {@link TypeSafePredicateBuilder} are passed to this method.
 *
 * @param <T> the type parameter to be lower-bounded
 *
 * @author Bartosz Popiela
 */
public sealed interface TypeSafe<T> permits TypeSafePredicateBuilder {
}
