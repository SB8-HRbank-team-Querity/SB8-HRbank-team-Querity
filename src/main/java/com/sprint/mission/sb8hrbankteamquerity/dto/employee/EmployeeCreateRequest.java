package com.sprint.mission.sb8hrbankteamquerity.dto.employee;

import java.time.Instant;

public record EmployeeCreateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    Instant hireDate,
    String memo
) {
}