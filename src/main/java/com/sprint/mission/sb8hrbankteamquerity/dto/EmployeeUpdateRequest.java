package com.sprint.mission.sb8hrbankteamquerity.dto;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;

import java.time.Instant;

public record EmployeeUpdateRequest(
    String name,
    String email,
    int departmentId,
    String position,
    Instant hireDate,
    EmployeeStatus status,
    String memo
) {
}
