package com.github.bartoszpop.jpa.specification;

import org.junit.jupiter.api.Test;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;

class CompositeSpecificationTest {

    @Test
    void toPredicate_delegatesToPredicateBuilder() {
        // given
        var targetRoot = new NoOpRoot<>();
        var targetQuery = new NoOpCriteriaQuery<>();
        var targetCriteriaBuilder = new NoOpCriteriaBuilder();
        var builderPredicate = new NoOpPredicate();
        var specification = CompositeSpecification.<Object, Path<Object>, TypeSafePredicateBuilder<Path<Object>>>of(
                (root, query, criteriaBuilder) -> {
                    if (root == targetRoot && query == targetQuery && criteriaBuilder == targetCriteriaBuilder) {
                        return builderPredicate;
                    } else {
                        return null;
                    }
                }
        );

        // when
        var specificationPredicate = specification.toPredicate(targetRoot, targetQuery, targetCriteriaBuilder);

        // then
        assertThat(specificationPredicate, sameInstance(builderPredicate));
    }

    @Test
    void asBuilder_returnsDelegatePredicateBuilder() {
        // given
        var delegatePredicateBuilder = ((TypeSafePredicateBuilder<Path<Object>>) (root, query, criteriaBuilder) -> new NoOpPredicate());
        var specification = CompositeSpecification.of(delegatePredicateBuilder);

        // when
        var specificationPredicateBuilder = specification.asBuilder();

        // then
        assertThat(specificationPredicateBuilder, sameInstance(delegatePredicateBuilder));
    }

    @Test
    void not_delegatesToCriteriaBuilder() {
        // given
        var negatedPredicate = new NoOpPredicate();
        var predicate = new AbstractPredicate() {
            @Override
            public Predicate not() {
                return negatedPredicate;
            }
        };
        var negatingCriteriaBuilder = new AbstractCriteriaBuilder() {
            @Override
            public Predicate not(Expression<Boolean> restriction) {
                return restriction instanceof Predicate ? ((Predicate) restriction).not() : null;
            }
        };
        var specification = CompositeSpecification.<Object, Path<Object>, TypeSafePredicateBuilder<Path<Object>>>of(
                (root, query, criteriaBuilder) -> predicate
        );

        // when
        var negatedSpecification = CompositeSpecification.not(specification);
        var negatedSpecificationPredicate = negatedSpecification.toPredicate(new NoOpRoot<>(), new NoOpCriteriaQuery<>(), negatingCriteriaBuilder);

        // then
        assertThat(negatedSpecificationPredicate, sameInstance(negatedPredicate));
    }

    @Test
    void and_delegatesToCriteriaBuilder() {
        // given
        var leftSpecificationPredicate = new NoOpPredicate();
        var leftSpecification = CompositeSpecification.<Object, Path<Object>, TypeSafePredicateBuilder<Path<Object>>>of(
                (root, query, criteriaBuilder) -> leftSpecificationPredicate
        );
        var rightSpecificationPredicate = new NoOpPredicate();
        var rightSpecification = CompositeSpecification.<Object, Path<Object>, TypeSafePredicateBuilder<Path<Object>>>of(
                (root, query, criteriaBuilder) -> rightSpecificationPredicate
        );
        var conjunctiveCriteriaBuilder = new AbstractCriteriaBuilder() {
            @Override
            public Predicate and(Expression<Boolean> left, Expression<Boolean> right) {
                if (left instanceof Predicate && right instanceof Predicate) {
                    return new CompositePredicate(List.of((Predicate) left, (Predicate) right));
                } else {
                    return null;
                }
            }
        };

        // when
        var combinedSpecification = leftSpecification.and(rightSpecification);
        var combinedPredicate = combinedSpecification.toPredicate(new NoOpRoot<>(), new NoOpCriteriaQuery<>(), conjunctiveCriteriaBuilder);

        // then
        assertThat(combinedPredicate, instanceOf(CompositePredicate.class));
        assertThat(((CompositePredicate) combinedPredicate).getDelegates(), containsInAnyOrder(sameInstance(leftSpecificationPredicate), sameInstance(rightSpecificationPredicate)));
    }

    @Test
    void or_delegatesToCriteriaBuilder() {
        // given
        var leftSpecificationPredicate = new NoOpPredicate();
        var leftSpecification = CompositeSpecification.<Object, Path<Object>, TypeSafePredicateBuilder<Path<Object>>>of(
                (root, query, criteriaBuilder) -> leftSpecificationPredicate
        );
        var rightSpecificationPredicate = new NoOpPredicate();
        var rightSpecification = CompositeSpecification.<Object, Path<Object>, TypeSafePredicateBuilder<Path<Object>>>of(
                (root, query, criteriaBuilder) -> rightSpecificationPredicate
        );
        var disjunctiveCriteriaBuilder = new AbstractCriteriaBuilder() {
            @Override
            public Predicate or(Expression<Boolean> left, Expression<Boolean> right) {
                if (left instanceof Predicate && right instanceof Predicate) {
                    return new CompositePredicate(List.of((Predicate) left, (Predicate) right));
                } else {
                    return null;
                }
            }
        };

        // when
        var combinedSpecification = leftSpecification.or(rightSpecification);
        var combinedPredicate = combinedSpecification.toPredicate(new NoOpRoot<>(), new NoOpCriteriaQuery<>(), disjunctiveCriteriaBuilder);

        // then
        assertThat(combinedPredicate, instanceOf(CompositePredicate.class));
        assertThat(((CompositePredicate) combinedPredicate).getDelegates(), containsInAnyOrder(sameInstance(leftSpecificationPredicate), sameInstance(rightSpecificationPredicate)));
    }
}
