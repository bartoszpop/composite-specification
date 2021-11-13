package com.github.bartoszpop.jpa.specification.example;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;

import static org.hamcrest.Matchers.equalTo;

public final class EmployeeMatchers {
    private EmployeeMatchers() {
    }

    public static CombinableMatcher<Employee> employee(Employee employee) {
        return employee(equalTo(employee));
    }

    public static CombinableMatcher<Employee> employee(Matcher<? super Employee> employeeMatcher) {
        return new CombinableMatcher<>(new FeatureMatcher<Employee, Employee>(employeeMatcher, "is employee", "employee") {
            @Override
            protected Employee featureValueOf(Employee actual) {
                return actual;
            }
        });
    }
}
