package io.github.bartoszpop.jpa.specification;


import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * This class creates {@link Predicate} for a target of type {@code T}.
 * {@link CompositeSpecification} restricts {@code T} to be a supertype of {@link Root}
 * and delegates to this class to create {@link Predicate} for {@link Specification}.
 *
 * @param <T> the type of a target the predicate evaluates on
 * @author Bartosz Popiela
 */
public interface PredicateBuilder<T> {
    Predicate toPredicate(T target, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);
}
