# Composite Specification API

This project provides a composite [Specification](../main/src/main/java/io/github/bartoszpop/jpa/specification/CompositeSpecification.java)
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
More examples can be found [here](../main/src/example/java/io/github/bartoszpop/jpa/specification/example/DepartmentApplication.java). Run the demo application with
```shell
mvn org.springframework.boot:spring-boot-maven-plugin:run -P example
```

## License

Distributed under the MIT license. See [LICENSE](../main/LICENSE) for more information.
