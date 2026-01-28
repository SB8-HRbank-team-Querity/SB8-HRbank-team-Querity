package com.sprint.mission.sb8hrbankteamquerity.dto.employee;

import com.sprint.mission.sb8hrbankteamquerity.entity.enums.EmployeeStatus;

import java.time.LocalDate;

public record EmployeeDto(
    Long id,
    String name,
    String email,
    String employeeNumber,
    Long departmentId,
    String departmentName,
    String position,
    LocalDate hireDate,
    EmployeeStatus status,
    Long profileImageId
) {
}
