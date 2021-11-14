package com.github.bartoszpop.jpa.specification.example;

import com.github.bartoszpop.jpa.specification.CompositeSpecification;
import com.github.bartoszpop.jpa.specification.ExpressionSpecifications;
import com.github.bartoszpop.jpa.specification.TypeSafePredicateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import java.time.LocalDate;
import java.util.List;

import static com.github.bartoszpop.jpa.specification.CompositeSpecification.not;
import static com.github.bartoszpop.jpa.specification.ExpressionSpecifications.greaterThanOrEqualTo;
import static com.github.bartoszpop.jpa.specification.ExpressionSpecifications.in;
import static com.github.bartoszpop.jpa.specification.ExpressionSpecifications.lessThan;
import static com.github.bartoszpop.jpa.specification.example.DepartmentMatchers.department;
import static com.github.bartoszpop.jpa.specification.example.DepartmentSpecifications.fetchEmployees;
import static com.github.bartoszpop.jpa.specification.example.DepartmentSpecifications.joinEmployees;
import static com.github.bartoszpop.jpa.specification.example.DepartmentSpecifications.name;
import static com.github.bartoszpop.jpa.specification.example.EmployeeMatchers.employee;
import static com.github.bartoszpop.jpa.specification.example.EmployeeSpecifications.dateOfBirth;
import static com.github.bartoszpop.jpa.specification.example.EmployeeSpecifications.firstName;
import static com.github.bartoszpop.jpa.specification.example.EmployeeSpecifications.secondName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@SpringBootApplication
public class DepartmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepartmentApplication.class, args);
    }

    @Autowired
    public void friendsCompany(DepartmentRepository departmentRepository) {
        var chandler = new Employee("Chandler", "Bing", LocalDate.of(1968, 4, 8));
        var dina = new Employee("Dina", "Tribbiani", LocalDate.of(1980, 12, 19));
        var joey = new Employee("Joey", "Tribbiani", LocalDate.of(1968, 1, 9));
        var monica = new Employee("Monica", "Geller", LocalDate.of(1970, 3, 22));
        var phoebe = new Employee("Phoebe", "Buffay", LocalDate.of(1967, 2, 16));
        var rachel = new Employee("Rachel", "Green", LocalDate.of(1969, 5, 5));
        var ross = new Employee("Ross", "Geller", LocalDate.of(1967, 10, 18));

        var salesDepartment = new Department("Sales");
        salesDepartment.addEmployee(chandler);
        salesDepartment.addEmployee(joey);
        salesDepartment.addEmployee(monica);
        salesDepartment.addEmployee(phoebe);
        salesDepartment.addEmployee(ross);
        departmentRepository.save(salesDepartment);

        var financeDepartment = new Department("Finance");
        financeDepartment.addEmployee(rachel);
        departmentRepository.save(financeDepartment);

        var hrDepartment = new Department("Human Resources");
        hrDepartment.addEmployee(dina);
        departmentRepository.save(hrDepartment);

        /*
        select
           department0_.id as id1_0_,
           department0_.name as name2_0_
        from
           department department0_
        where
           department0_.name =?
         */
        var departmentFound = departmentRepository.findOne(name(salesDepartment.getName())).orElseThrow(() -> new DepartmentNotFoundException(salesDepartment.getName()));
        assertThat(departmentFound, equalTo(salesDepartment));

        /*
        select distinct
           department0_.id as id1_0_,
           department0_.name as name2_0_
        from
           department department0_
           left outer join
              employee employees1_
              on department0_.id = employees1_.department_id
        where
           employees1_.first_name =?
           and employees1_.second_name =?
         */
        var departmentsFound = departmentRepository.findAll(joinEmployees(firstName(chandler.getFirstName()).and(secondName(chandler.getSecondName()))));
        assertThat(departmentsFound, contains(department(salesDepartment)));

        /*
        select distinct
           department0_.id as id1_0_,
           department0_.name as name2_0_
        from
           department department0_
           left outer join
              employee employees1_
              on department0_.id = employees1_.department_id
        where
           employees1_.second_name =?
           or employees1_.second_name =?
         */
        departmentsFound = departmentRepository.findAll(joinEmployees(secondName(chandler.getSecondName()).or(secondName(dina.getSecondName()))));
        assertThat(departmentsFound, containsInAnyOrder(department(salesDepartment), department(hrDepartment)));

        /*
        select distinct
           department0_.id as id1_0_,
           department0_.name as name2_0_
        from
           department department0_
           left outer join
              employee employees1_
              on department0_.id = employees1_.department_id
        where
           employees1_.second_name <>?
         */
        departmentsFound = departmentRepository.findAll(joinEmployees(not(secondName(rachel.getSecondName()))));
        assertThat(departmentsFound, containsInAnyOrder(department(salesDepartment), department(hrDepartment)));

        /*
        select distinct
           department0_.id as id1_0_0_,
           employees1_.id as id1_1_1_,
           department0_.name as name2_0_0_,
           employees1_.date_of_birth as date_of_2_1_1_,
           employees1_.department_id as departme5_1_1_,
           employees1_.first_name as first_na3_1_1_,
           employees1_.second_name as second_n4_1_1_,
           employees1_.department_id as departme5_1_0__,
           employees1_.id as id1_1_0__
        from
           department department0_
           left outer join
              employee employees1_
              on department0_.id = employees1_.department_id
        where
           employees1_.second_name =?
         */
        departmentsFound = departmentRepository.findAll(fetchEmployees(secondName(joey.getSecondName())));
        assertThat(departmentsFound, containsInAnyOrder(department(salesDepartment), department(hrDepartment)));

        var salesDepartmentFound = departmentsFound.stream().filter(salesDepartment::equals).findFirst().orElseThrow(() -> new DepartmentNotFoundException(salesDepartment.getName()));
        assertThat(salesDepartmentFound.getEmployees(), contains(employee(joey)));

        var hrDepartmentFound = departmentsFound.stream().filter(hrDepartment::equals).findFirst().orElseThrow(() -> new DepartmentNotFoundException(hrDepartment.getName()));
        assertThat(hrDepartmentFound.getEmployees(), contains(employee(dina)));

        /*
        select distinct
           department0_.id as id1_0_0_,
           employees1_.id as id1_1_1_,
           department0_.name as name2_0_0_,
           employees1_.date_of_birth as date_of_2_1_1_,
           employees1_.department_id as departme5_1_1_,
           employees1_.first_name as first_na3_1_1_,
           employees1_.second_name as second_n4_1_1_,
           employees1_.department_id as departme5_1_0__,
           employees1_.id as id1_1_0__
        from
           department department0_
           left outer join
              employee employees1_
              on department0_.id = employees1_.department_id
        where
           employees1_.date_of_birth >=?
           and employees1_.date_of_birth <?
         */
        departmentsFound = departmentRepository.findAll(fetchEmployees(dateOfBirth(greaterThanOrEqualTo(LocalDate.of(1967, 1, 1)).and(lessThan(LocalDate.of(1968, 1, 1))))));
        assertThat(departmentsFound, contains(department(salesDepartment)));

        salesDepartmentFound = departmentsFound.stream().filter(salesDepartment::equals).findFirst().orElseThrow(() -> new DepartmentNotFoundException(salesDepartment.getName()));
        assertThat(salesDepartmentFound.getEmployees(), containsInAnyOrder(employee(phoebe), employee(ross)));

        /*
        select distinct
           department0_.id as id1_0_,
           department0_.name as name2_0_
        from
           department department0_
           left outer join
              employee employees1_
              on department0_.id = employees1_.department_id
        where
           employees1_.first_name =?
           or employees1_.id in
           (
               ?
           )
         */
        departmentsFound = departmentRepository.findAll(joinEmployees(firstName(phoebe.getFirstName()).or(in(List.of(rachel)))));
        assertThat(departmentsFound, containsInAnyOrder(department(salesDepartment), department(financeDepartment)));

        /*
        Because CompositeSpecification#or takes CompositeSpecification parametrized with a supertype,
        the first specification in chain must be parametrized with a common subtype of the subsequent specifications.
        In consequence, if the specifications are parametrized with different type arguments,
        it is not possible to change the order without an explicit cast.
        This flaw has been fixed in the Java 17 compatible variant of this library with use of a sealed interface.
         */
        departmentsFound = departmentRepository.findAll(joinEmployees(
                ((CompositeSpecification<Employee, Path<Employee>>) (CompositeSpecification<Employee, ? extends Expression<Employee>>) in(List.of(rachel)))
                .or(firstName(phoebe.getFirstName())))); // Java 11
        assertThat(departmentsFound, containsInAnyOrder(department(salesDepartment), department(financeDepartment)));

        departmentsFound = departmentRepository.findAll(joinEmployees(
                ExpressionSpecifications.<Employee, Path<Employee>, TypeSafePredicateBuilder<Path<Employee>>> in(List.of(rachel))
                        .or(firstName(phoebe.getFirstName())))); // Java 17
        assertThat(departmentsFound, containsInAnyOrder(department(salesDepartment), department(financeDepartment)));
    }
}
