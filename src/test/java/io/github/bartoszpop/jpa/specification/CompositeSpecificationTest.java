package io.github.bartoszpop.jpa.specification;

import org.junit.jupiter.api.Test;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class CompositeSpecificationTest {

    @Test
    void toPredicate_delegatesToPredicateBuilder() {
        // given
        var builderPredicate = new NoOpPredicate();
        var specificationRoot = new NoOpRoot<>();
        var specificationQuery = new NoOpCriteriaQuery<>();
        var specificationCriteriaBuilder = new NoOpCriteriaBuilder();
        var specification = CompositeSpecification.<Object, Path<Object>, TypeSafePredicateBuilder<Path<Object>>>of(
                (root, query, criteriaBuilder) -> {
                    if (Objects.equals(root, specificationRoot) && Objects.equals(query, specificationQuery) && Objects.equals(criteriaBuilder, specificationCriteriaBuilder)) {
                        return builderPredicate;
                    } else {
                        return null;
                    }
                }
        );

        // when
        var specificationPredicate = specification.toPredicate(specificationRoot, specificationQuery, specificationCriteriaBuilder);

        // then
        assertThat(specificationPredicate, equalTo(builderPredicate));
    }

    @Test
    void asBuilder_delegatesToPredicateBuilder() {
        // given
        var delegateBuilderPredicate = new NoOpPredicate();
        var specification = CompositeSpecification.of(((TypeSafePredicateBuilder<Path<Object>>) (root, query, criteriaBuilder) -> delegateBuilderPredicate));

        // when
        var builderPredicate = specification.asBuilder().toPredicate(new NoOpRoot<>(), new NoOpCriteriaQuery<>(), new NoOpCriteriaBuilder());

        // then
        assertThat(builderPredicate, equalTo(delegateBuilderPredicate));
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
        var negatedSpecificationPredicate = CompositeSpecification.not(specification).toPredicate(new NoOpRoot<>(), new NoOpCriteriaQuery<>(), negatingCriteriaBuilder);

        // then
        assertThat(negatedSpecificationPredicate, equalTo(negatedPredicate));
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
        var combinedPredicate = new NoOpPredicate();
        var conjunctiveCriteriaBuilder = new AbstractCriteriaBuilder() {
            @Override
            public Predicate and(Expression<Boolean> left, Expression<Boolean> right) {
                if (Objects.equals(left, leftSpecificationPredicate) && Objects.equals(right, rightSpecificationPredicate)) {
                    return combinedPredicate;
                } else {
                    return null;
                }
            }
        };

        // when
        var combinedSpecificationPredicate = leftSpecification.and(rightSpecification).toPredicate(new NoOpRoot<>(), new NoOpCriteriaQuery<>(), conjunctiveCriteriaBuilder);

        // then
        assertThat(combinedSpecificationPredicate, equalTo(combinedPredicate));
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
        var combinedPredicate = new NoOpPredicate();
        var disjunctiveCriteriaBuilder = new AbstractCriteriaBuilder() {
            @Override
            public Predicate or(Expression<Boolean> left, Expression<Boolean> right) {
                if (Objects.equals(left, leftSpecificationPredicate) && Objects.equals(right, rightSpecificationPredicate)) {
                    return combinedPredicate;
                } else {
                    return null;
                }
            }
        };

        // when
        var combinedSpecificationPredicate = leftSpecification.or(rightSpecification).toPredicate(new NoOpRoot<>(), new NoOpCriteriaQuery<>(), disjunctiveCriteriaBuilder);

        // then
        assertThat(combinedSpecificationPredicate, equalTo(combinedPredicate));
    }
}
