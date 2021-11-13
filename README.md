# Composite Specification API

This project provides a composite [Specification](../main/src/main/java/com/github/bartoszpop/jpa/specification/CompositeSpecification.java)
in the sense of ["Specifications"]("https://www.martinfowler.com/apsupp/spec.pdf") by Eric Evans and Martin Fowler.
It is parametrized with the type of a target the [Predicate](https://javaee.github.io/javaee-spec/javadocs/javax/persistence/criteria/Predicate.html) evaluates on.
Instances of this class are composable in opposite to the [Root](https://javaee.github.io/javaee-spec/javadocs/javax/persistence/criteria/Root.html) limited
[Specification](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/domain/Specification.html) interface.
The Specification API is a part of [Spring Data JPA](https://spring.io/projects/spring-data-jpa).

## Example

Consider the following entities:
```java
@Entity
public class Employee {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
    
    @Column
    private String firstName;

    @Column
    private LocalDate dateOfBirth;
    
    (...)
}
```
```java
@Entity
public class Department {

    @Id
    private Long id;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;
    
    (...)
}
```
and the specifications:
```java
public final class DepartmentSpecifications {
    
    public static CompositeSpecification<Department, From<?, Department>> fetchEmployees() {
        return CompositeSpecification.<Department, From<?, Department>, TypeSafePredicateBuilder<From<?, Department>>>of(
                (root, query, criteriaBuilder) -> {
                    root.fetch("employees", JoinType.LEFT);
                    return criteriaBuilder.and();
                });
    }
    
    public static CompositeSpecification<Department, From<?, Department>> joinEmployees(CompositeSpecification<?, ? super Join<?, Employee>> employeeSpecification) {
        return CompositeSpecification.<Department, From<?, Department>, TypeSafePredicateBuilder<From<?, Department>>>of(
                (root, query, criteriaBuilder) -> employeeSpecification.asBuilder().toPredicate(root.<Department, Employee>join("employees", JoinType.LEFT), query, criteriaBuilder));
    }
}

```
```java
public final class EmployeeSpecifications {
    
    public static CompositeSpecification<Employee, Path<Employee>> firstName(String firstName) {
        return CompositeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstName"), firstName)
        );
    }

    public static CompositeSpecification<Employee, Path<Employee>> dateOfBirth(LocalDate dateOfBirth) {
        return CompositeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfBirth"), dateOfBirth)
        );
    }
}
```
To find departments that have an employee whose first name is John and was born on 1.01.1990, compose the specifications as follows:
```java
var departments = departmentRepository.findAll(joinEmployees(firstName("John").and(dateOfBirth(LocalDate.of(1990, 1, 1)))));
```
This technique allows to fetch the lazy associations on a per-query basis.
```java
var department = departmentRepository.findOne(name("Sales").and(fetchEmployees()));
```
More examples can be found [here](../main/src/example/java/com/github/bartoszpop/jpa/specification/example/DepartmentApplication.java). Run the demo application with
```shell
mvn org.springframework.boot:spring-boot-maven-plugin:run -P example
```

## Java 11 vs Java 17

This library comes in two variants, with no classifier compiled with Java 17
```xml
<dependency>
    <groupId>com.github.bartoszpop</groupId>
    <artifactId>composite-specification</artifactId>
    <version>1.0.0</version>
</dependency>
```
and the backward compatible 
```xml
<dependency>
    <groupId>com.github.bartoszpop</groupId>
    <artifactId>composite-specification</artifactId>
    <version>1.0.0</version>
    <classifier>jdk11</classifier>
</dependency>
```
with the ```jdk11``` classifier compiled with Java 11. The difference between these is that the former imposes invariants on the type parameter
with a package-private [interface](../main/src/main/java/com/github/bartoszpop/jpa/specification/TypeSafe.java),
while the latter sealed this interface to overcome the necessity of an explicit cast when chaining specifications
parametrized with different type arguments.

Consider the following specifications:
```java
public final class ExpressionSpecifications {
    private ExpressionSpecifications() {
    }

    public static <T> CompositeSpecification<T, Expression<T>> in(Collection<T> objects) {
        return CompositeSpecification.<T, Expression<T>, TypeSafePredicateBuilder<Expression<T>>>of(
                (root, query, criteriaBuilder) -> root.in(objects)
        );
    }
    
    (...)
}
```
```java
public final class EmployeeSpecifications {

    private EmployeeSpecifications() {
    }

    public static CompositeSpecification<Employee, Path<Employee>> firstName(String firstName) {
        return CompositeSpecification.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>>of(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstName"), firstName)
        );
    }
    
    (...)
}
```
Because [Expression](https://javaee.github.io/javaee-spec/javadocs/javax/persistence/criteria/Expression.html)
is a supertype of [Path](https://javaee.github.io/javaee-spec/javadocs/javax/persistence/criteria/Path.html) and
```java
public final class CompositeSpecification<T, S> implements Specification<T> {
    public CompositeSpecification<T, S> or(CompositeSpecification<T, ? super S> other) {
        (...)
    }
}
```
is parametrized with a supertype of ```S```, the first specification in chain must be parametrized with a common subtype of the subsequent specifications
```java
firstName("John").or(in(List.of(bob)))
```
To compose the specifications in the reverse order an explicit cast is necessary
```java
((CompositeSpecification<Employee, Path<Employee>>) (CompositeSpecification<Employee, ? extends Expression<Employee>>) in(List.of(bob))).or(firstName("John"))));
```

This is no longer a case with a sealed interface
```java
ExpressionSpecifications.<Employee, Path<Employee>>in(List.of(bob)).or(firstName("John")))));
```

## License

Distributed under the MIT license. See [LICENSE](../main/LICENSE) for more information.
