package com.sprint.mission.sb8hrbankteamquerity.dto.employee;

import com.sprint.mission.sb8hrbankteamquerity.entity.enums.EmployeeStatus;

import java.time.LocalDate;

public record EmployeeUpdateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    LocalDate hireDate,
    EmployeeStatus status,
    String memo
) {
}
