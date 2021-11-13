package com.github.bartoszpop.jpa.specification.example;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.CombinableMatcher;

import static org.hamcrest.Matchers.equalTo;

public final class DepartmentMatchers {
    private DepartmentMatchers() {
    }

    public static CombinableMatcher<Department> department(Department department) {
        return department(equalTo(department));
    }

    public static CombinableMatcher<Department> department(Matcher<? super Department> departmentMatcher) {
        return new CombinableMatcher<>(new FeatureMatcher<Department, Department>(departmentMatcher, "is department", "department") {
            @Override
            protected Department featureValueOf(Department actual) {
                return actual;
            }
        });
    }
}
