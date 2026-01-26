package com.sprint.mission.sb8hrbankteamquerity.dto.employee;

import java.time.LocalDate;

public record EmployeeCreateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    LocalDate hireDate,
    String memo
) {
}