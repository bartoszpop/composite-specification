package io.github.bartoszpop.jpa.specification;

/**
 * This class is a workaround for the lower-bound of {@link PredicateBuilder} in {@link CompositeSpecification#of} to be restricted.
 * It must be package-private to ensure that only instances of {@link TypeSafePredicateBuilder} are passed to this method.
 * Since Java 17 it may be a sealed interface and permit {@link TypeSafePredicateBuilder} only.
 *
 * @param <T> the type parameter to be lower-bounded
 * @author Bartosz Popiela
 */
interface TypeSafe<T> {
}
