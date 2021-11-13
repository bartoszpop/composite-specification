package com.github.bartoszpop.jpa.specification.example;

public final class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String departmentName) {
        super("Department " + departmentName + " has not been found.");
    }
}
