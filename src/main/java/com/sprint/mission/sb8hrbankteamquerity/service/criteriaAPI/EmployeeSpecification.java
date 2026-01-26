package com.sprint.mission.sb8hrbankteamquerity.service.criteriaAPI;

import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EmployeeSpecification {

    public static Specification<Employee> nameOrEmailContains(String nameOrEmail) {
        return ((root, query, cb) -> {
            if (nameOrEmail == null || nameOrEmail.isBlank()) return null;
            return cb.or(
                cb.like(root.get("name"), "%" + nameOrEmail + "%"),
                cb.like(root.get("email"), "%" + nameOrEmail + "%")
            );
        });
    }

    public static Specification<Employee> employeeNumberContains(String employeeNumber) {
        return ((root, query, cb) ->
            employeeNumber == null ? null : cb.like(root.get("employeeNumber"), "%" + employeeNumber + "%"));
    }

    public static Specification<Employee> departmentNameContains(String departmentName) {
        return ((root, query, cb) ->
            departmentName == null ? null : cb.like(root.join("departmentId").get("name"), "%" + departmentName + "%"));
    }

    public static Specification<Employee> positionContains(String position) {
        return ((root, query, cb) ->
            position == null ? null : cb.like(root.get("position"), "%" + position + "%"));
    }

    public static Specification<Employee> hireDateBetween(LocalDate hireDateFrom, LocalDate hireDateTo) {
        return ((root, query, cb) -> {
            if (hireDateFrom != null && hireDateTo != null) {
                return cb.between(root.get("hireDate"), hireDateFrom, hireDateTo);
            } else if (hireDateFrom != null) {
                return cb.greaterThanOrEqualTo(root.get("hireDate"), hireDateFrom);
            } else if (hireDateTo != null) {
                return cb.lessThanOrEqualTo(root.get("hireDate"), hireDateTo);
            } else return null;
        });
    }

    public static Specification<Employee> statusEquals(EmployeeStatus status) {
        return ((root, query, cb) ->
            status == null ? null : cb.equal(root.get("status"), status));
    }
}

