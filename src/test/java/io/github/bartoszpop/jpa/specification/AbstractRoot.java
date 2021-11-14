package io.github.bartoszpop.jpa.specification;

import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractRoot<T> implements Root<T> {

    @Override
    public EntityType<T> getModel() {
        return null;
    }

    @Override
    public Path<?> getParentPath() {
        return null;
    }

    @Override
    public <Y> Path<Y> get(SingularAttribute<? super T, Y> attribute) {
        return null;
    }

    @Override
    public <E, C extends Collection<E>> Expression<C> get(PluralAttribute<T, C, E> collection) {
        return null;
    }

    @Override
    public <K, V, M extends Map<K, V>> Expression<M> get(MapAttribute<T, K, V> map) {
        return null;
    }

    @Override
    public Expression<Class<? extends T>> type() {
        return null;
    }

    @Override
    public <Y> Path<Y> get(String attributeName) {
        return null;
    }

    @Override
    public Set<Join<T, ?>> getJoins() {
        return null;
    }

    @Override
    public boolean isCorrelated() {
        return false;
    }

    @Override
    public From<T, T> getCorrelationParent() {
        return null;
    }

    @Override
    public <Y> Join<T, Y> join(SingularAttribute<? super T, Y> attribute) {
        return null;
    }

    @Override
    public <Y> Join<T, Y> join(SingularAttribute<? super T, Y> attribute, JoinType jt) {
        return null;
    }

    @Override
    public <Y> CollectionJoin<T, Y> join(CollectionAttribute<? super T, Y> collection) {
        return null;
    }

    @Override
    public <Y> SetJoin<T, Y> join(SetAttribute<? super T, Y> set) {
        return null;
    }

    @Override
    public <Y> ListJoin<T, Y> join(ListAttribute<? super T, Y> list) {
        return null;
    }

    @Override
    public <K, V> MapJoin<T, K, V> join(MapAttribute<? super T, K, V> map) {
        return null;
    }

    @Override
    public <Y> CollectionJoin<T, Y> join(CollectionAttribute<? super T, Y> collection, JoinType jt) {
        return null;
    }

    @Override
    public <Y> SetJoin<T, Y> join(SetAttribute<? super T, Y> set, JoinType jt) {
        return null;
    }

    @Override
    public <Y> ListJoin<T, Y> join(ListAttribute<? super T, Y> list, JoinType jt) {
        return null;
    }

    @Override
    public <K, V> MapJoin<T, K, V> join(MapAttribute<? super T, K, V> map, JoinType jt) {
        return null;
    }

    @Override
    public <X, Y> Join<X, Y> join(String attributeName) {
        return null;
    }

    @Override
    public <X, Y> CollectionJoin<X, Y> joinCollection(String attributeName) {
        return null;
    }

    @Override
    public <X, Y> SetJoin<X, Y> joinSet(String attributeName) {
        return null;
    }

    @Override
    public <X, Y> ListJoin<X, Y> joinList(String attributeName) {
        return null;
    }

    @Override
    public <X, K, V> MapJoin<X, K, V> joinMap(String attributeName) {
        return null;
    }

    @Override
    public <X, Y> Join<X, Y> join(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public <X, Y> CollectionJoin<X, Y> joinCollection(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public <X, Y> SetJoin<X, Y> joinSet(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public <X, Y> ListJoin<X, Y> joinList(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public <X, K, V> MapJoin<X, K, V> joinMap(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public Predicate isNull() {
        return null;
    }

    @Override
    public Predicate isNotNull() {
        return null;
    }

    @Override
    public Predicate in(Object... values) {
        return null;
    }

    @Override
    public Predicate in(Expression<?>... values) {
        return null;
    }

    @Override
    public Predicate in(Collection<?> values) {
        return null;
    }

    @Override
    public Predicate in(Expression<Collection<?>> values) {
        return null;
    }

    @Override
    public <X> Expression<X> as(Class<X> type) {
        return null;
    }

    @Override
    public Set<Fetch<T, ?>> getFetches() {
        return null;
    }

    @Override
    public <Y> Fetch<T, Y> fetch(SingularAttribute<? super T, Y> attribute) {
        return null;
    }

    @Override
    public <Y> Fetch<T, Y> fetch(SingularAttribute<? super T, Y> attribute, JoinType jt) {
        return null;
    }

    @Override
    public <Y> Fetch<T, Y> fetch(PluralAttribute<? super T, ?, Y> attribute) {
        return null;
    }

    @Override
    public <Y> Fetch<T, Y> fetch(PluralAttribute<? super T, ?, Y> attribute, JoinType jt) {
        return null;
    }

    @Override
    public <X, Y> Fetch<X, Y> fetch(String attributeName) {
        return null;
    }

    @Override
    public <X, Y> Fetch<X, Y> fetch(String attributeName, JoinType jt) {
        return null;
    }

    @Override
    public Selection<T> alias(String name) {
        return null;
    }

    @Override
    public boolean isCompoundSelection() {
        return false;
    }

    @Override
    public List<Selection<?>> getCompoundSelectionItems() {
        return null;
    }

    @Override
    public Class<? extends T> getJavaType() {
        return null;
    }

    @Override
    public String getAlias() {
        return null;
    }
}
