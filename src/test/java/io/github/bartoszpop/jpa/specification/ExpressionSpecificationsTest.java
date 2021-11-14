package io.github.bartoszpop.jpa.specification;

import org.junit.jupiter.api.Test;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ExpressionSpecificationsTest {

    @Test
    void equal_delegatesToCriteriaBuilder() {
        // given
        var value = Integer.valueOf(1);
        var root = new NoOpRoot<Integer>();
        var equalPredicate = new NoOpPredicate();
        var criteriaBuilder = new AbstractCriteriaBuilder() {
            @Override
            public Predicate equal(Expression<?> x, Object y) {
                if (Objects.equals(x, root) && Objects.equals(y, value)) {
                    return equalPredicate;
                } else {
                    return null;
                }
            }
        };

        // when
        var equalSpecificationPredicate = ExpressionSpecifications.<Integer, Root<Integer>> equal(value).toPredicate(root, new NoOpCriteriaQuery<>(), criteriaBuilder);

        // then
        assertThat(equalSpecificationPredicate, equalTo(equalPredicate));
    }

    @Test
    void greaterThan_delegatesToCriteriaBuilder() {
        // given
        var value = Integer.valueOf(1);
        var root = new NoOpRoot<Integer>();
        var greaterThanPredicate = new NoOpPredicate();
        var criteriaBuilder = new AbstractCriteriaBuilder() {
            @Override
            public <Y extends Comparable<? super Y>> Predicate greaterThan(Expression<? extends Y> x, Y y) {
                if (Objects.equals(x, root) && Objects.equals(y, value)) {
                    return greaterThanPredicate;
                } else {
                    return null;
                }
            }
        };

        // when
        var greaterThanSpecificationPredicate = ExpressionSpecifications.<Integer, Root<Integer>> greaterThan(value).toPredicate(root, new NoOpCriteriaQuery<>(), criteriaBuilder);

        // then
        assertThat(greaterThanSpecificationPredicate, equalTo(greaterThanPredicate));
    }

    @Test
    void greaterThanOrEqualTo_delegatesToCriteriaBuilder() {
        // given
        var value = Integer.valueOf(1);
        var root = new NoOpRoot<Integer>();
        var greaterThanOrEqualToPredicate = new NoOpPredicate();
        var criteriaBuilder = new AbstractCriteriaBuilder() {
            @Override
            public <Y extends Comparable<? super Y>> Predicate greaterThanOrEqualTo(Expression<? extends Y> x, Y y) {
                if (Objects.equals(x, root) && Objects.equals(y, value)) {
                    return greaterThanOrEqualToPredicate;
                } else {
                    return null;
                }
            }
        };

        // when
        var greaterThanOrEqualToSpecificationPredicate = ExpressionSpecifications.<Integer, Root<Integer>> greaterThanOrEqualTo(value).toPredicate(root, new NoOpCriteriaQuery<>(), criteriaBuilder);

        // then
        assertThat(greaterThanOrEqualToSpecificationPredicate, equalTo(greaterThanOrEqualToPredicate));
    }

    @Test
    void lessThan_delegatesToCriteriaBuilder() {
        // given
        var value = Integer.valueOf(1);
        var root = new NoOpRoot<Integer>();
        var lessThanPredicate = new NoOpPredicate();
        var criteriaBuilder = new AbstractCriteriaBuilder() {
            @Override
            public <Y extends Comparable<? super Y>> Predicate lessThan(Expression<? extends Y> x, Y y) {
                if (Objects.equals(x, root) && Objects.equals(y, value)) {
                    return lessThanPredicate;
                } else {
                    return null;
                }
            }
        };

        // when
        var lessThanSpecificationPredicate = ExpressionSpecifications.<Integer, Root<Integer>> lessThan(value).toPredicate(root, new NoOpCriteriaQuery<>(), criteriaBuilder);

        // then
        assertThat(lessThanSpecificationPredicate, equalTo(lessThanPredicate));
    }

    @Test
    void lessThanOrEqualTo_delegatesToCriteriaBuilder() {
        // given
        var value = Integer.valueOf(1);
        var root = new NoOpRoot<Integer>();
        var lessThanOrEqualToPredicate = new NoOpPredicate();
        var criteriaBuilder = new AbstractCriteriaBuilder() {
            @Override
            public <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(Expression<? extends Y> x, Y y) {
                if (Objects.equals(x, root) && Objects.equals(y, value)) {
                    return lessThanOrEqualToPredicate;
                } else {
                    return null;
                }
            }
        };

        // when
        var lessThanOrEqualToSpecificationPredicate = ExpressionSpecifications.<Integer, Root<Integer>> lessThanOrEqualTo(value).toPredicate(root, new NoOpCriteriaQuery<>(), criteriaBuilder);

        // then
        assertThat(lessThanOrEqualToSpecificationPredicate, equalTo(lessThanOrEqualToPredicate));
    }

    @Test
    void in_builderDelegatesToExpression() {
        // given
        var inValues = List.of(new Object(), new Object());
        var inPredicate = new NoOpPredicate();
        var root = new AbstractRoot<>() {
            @Override
            public Predicate in(Collection<?> values) {
                if (Objects.equals(inValues, values)) {
                    return inPredicate;
                } else {
                    return null;
                }
            }
        };

        // when
        var inSpecificationPredicate = ExpressionSpecifications.in(inValues).toPredicate(root, new NoOpCriteriaQuery<>(), new NoOpCriteriaBuilder());

        // then
        assertThat(inSpecificationPredicate, equalTo(inPredicate));
    }
}
