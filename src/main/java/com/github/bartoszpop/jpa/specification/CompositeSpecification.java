package com.github.bartoszpop.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * This class is a composite {@link Specification} in the sense of <a href="https://www.martinfowler.com/apsupp/spec.pdf">"Specifications"</a> by Eric Evans and Martin Fowler.
 * <p>
 * It is parameterized with a supertype of {@link Root} that it depends on to construct {@link Predicate}.
 * <br>
 * Because {@link CompositeSpecification#asBuilder()} returns {@link PredicateBuilder} applicable to this type, thus an instance of this class
 * <br>
 * may delegate to an instance parameterized with a different type argument.
 * <p>
 * Consider the following entities
 * <pre>
 * &#64;Entity
 * public class Employee {
 *
 *     &#64;ManyToOne(fetch = FetchType.LAZY)
 *     private Department department;
 *
 *     &#64;Column
 *     private String firstName;
 *
 *     &#64;Column
 *     private LocalDate dateOfBirth;
 *
 *     (...)
 * }
 *
 * &#64;Entity
 * public class Department {
 *
 *     &#64;OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
 *     private List&#60;Employee&#62; employees;
 *
 *     (...)
 * }
 * </pre>
 * and the specifications
 * <pre>{@code
 * public final class DepartmentSpecifications {
 *     private DepartmentSpecifications() {
 *     }
 *
 *     public static TypeSafeSpecification<Department, From<?, Department>> joinEmployees(TypeSafeSpecification<?, ? super Join<?, Employee>> employeeSpecification) {
 *         return TypeSafeSpecification.<Department, From<?, Department>, TypeSafePredicateBuilder<From<?, Department>>> of(
 *                 (root, query, criteriaBuilder) ->
 *                     employeeSpecification.asBuilder().toPredicate(root.<Department, Employee>join("employees", JoinType.LEFT), query, criteriaBuilder));
 *     }
 *
 *     public static TypeSafeSpecification<Department, From<?, Department>> fetchEmployees() {
 *         return TypeSafeSpecification.<Department, From<?, Department>, TypeSafePredicateBuilder<From<?, Department>>> of(
 *                 (root, query, criteriaBuilder) -> {
 *                     root.fetch("employees", JoinType.LEFT);
 *                     return criteriaBuilder.and();
 *                 });
 *     }
 * }
 *
 * public final class EmployeeSpecifications {
 *
 *     private EmployeeSpecifications() {
 *     }
 *
 *     public static TypeSafeSpecification<Employee, Path<Employee>> name(String name) {
 *         return TypeSafeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>> of(
 *                 (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name)
 *         );
 *     }
 *
 *     public static TypeSafeSpecification<Employee, Path<Employee>> dateOfBirth(LocalDate dateOfBirth) {
 *         return TypeSafeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>> of(
 *                 (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfBirth"), dateOfBirth)
 *         );
 *     }
 * }
 * }</pre>
 * To find departments that have an employee whose first name is John and was born on 1.01.1990, compose the specifications as follows:
 * <pre>{@code
 * var departments = departmentRepository.findAll(joinEmployees(firstName("John").and(dateOfBirth(LocalDate.of(1990, 1, 1)))));
 * }</pre>
 * This technique allows to fetch the LAZY associations on a per-query basis.
 * <pre>{@code
 * var department = departmentRepository.findOne(name("Sales").and(fetchEmployees()));
 * }</pre>
 * See <a href="https://github.com/bartoszpop/composite-specification/tree/main/src/example/">Composite Specification</a> for more examples.
 *
 * @param <T> the type of the entity
 * @param <S> the type of a target the predicate evaluates on
 * @author Bartosz Popiela
 */
public final class CompositeSpecification<T, S> implements Specification<T> {

    private final PredicateBuilder<S> predicateBuilder;

    /**
     * This constructor must be private because when parameterized with {@code <U extends PredicateBuilder<S> & TypeSafe<? super Root<T>>>},
     * the Java compiler does not restrict the type arguments of {@link PredicateBuilder} if the constructor argument is a lambda expression,
     * e.g. the type of lambda expression in {@code new TypeSafeSpecification<String, Number>((target, query, criteriaBuilder) -> {...})}
     * is {@code PredicateBuilder<String> & TypeSafe<? super Root<Number>>}.
     * <p>
     * If {@link TypeSafe} is package-private, the above statement throws {@link IllegalAccessError}.
     * <p>
     * If {@link TypeSafe} is sealed and permits {@link TypeSafePredicateBuilder} only, it throws {@link IncompatibleClassChangeError}.
     */
    private CompositeSpecification(PredicateBuilder<S> predicateBuilder) {
        this.predicateBuilder = predicateBuilder;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // Cast allowed because only instances of TypeSafePredicateBuilder<S>, where S is a supertype of Root<T> may be passed to TypeSafeSpecification#of
        //noinspection unchecked
        return ((PredicateBuilder<? super Root<T>>) (PredicateBuilder<?>) predicateBuilder).toPredicate(root, query, criteriaBuilder);
    }

    /**
     * Creates an instance of {@code TypeSafeSpecification<T, S>}, where {@code S} is a supertype of {@code Root<T>}.
     * This is because {@link TypeSafePredicateBuilder} is the only interface derived from {@link TypeSafe}
     * and it implements {@link PredicateBuilder} and {@link TypeSafe} with the same type argument {@code T}.
     */
    public static <T, S, U extends PredicateBuilder<S> & TypeSafe<? super Root<T>>> CompositeSpecification<T, S> of(U predicateBuilder) {
        return new CompositeSpecification<>(predicateBuilder);
    }

    public PredicateBuilder<S> asBuilder() {
        return predicateBuilder;
    }

    public static <T, S> CompositeSpecification<T, S> noOp() {
        return new CompositeSpecification<>((root, query, criteriaBuilder) -> criteriaBuilder.and());
    }

    public static <T, S> CompositeSpecification<T, S> not(CompositeSpecification<T, S> specification) {
        return new CompositeSpecification<>((root, query, criteriaBuilder) ->
                criteriaBuilder.not(specification.asBuilder().toPredicate(root, query, criteriaBuilder)));
    }

    public CompositeSpecification<T, S> and(CompositeSpecification<T, ? super S> other) {
        return new CompositeSpecification<>((root, query, criteriaBuilder) -> {
            var left = this.asBuilder().toPredicate(root, query, criteriaBuilder);
            var right = other.asBuilder().toPredicate(root, query, criteriaBuilder);
            return criteriaBuilder.and(left, right);
        });
    }

    public CompositeSpecification<T, S> or(CompositeSpecification<T, ? super S> other) {
        return new CompositeSpecification<>((root, query, criteriaBuilder) -> {
            var left = this.asBuilder().toPredicate(root, query, criteriaBuilder);
            var right = other.asBuilder().toPredicate(root, query, criteriaBuilder);
            return criteriaBuilder.or(left, right);
        });
    }
}
