package com.github.bartoszpop.jpa.specification.example;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long>, JpaSpecificationExecutor<Department> {
}
