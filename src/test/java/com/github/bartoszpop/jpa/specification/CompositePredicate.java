package com.github.bartoszpop.jpa.specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

public final class CompositePredicate extends AbstractPredicate {
    private final List<Predicate> delegates;

    public CompositePredicate(List<Predicate> delegates) {
        this.delegates = delegates;
    }

    public List<Predicate> getDelegates() {
        return delegates;
    }
}
