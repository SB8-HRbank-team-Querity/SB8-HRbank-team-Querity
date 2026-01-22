package com.sprint.mission.sb8hrbankteamquerity.dto;

import com.sprint.mission.sb8hrbankteamquerity.entity.Employee.Status;

import java.time.Instant;

public record EmployeeUpdateRequest(
    String name,
    String email,
    int departmentId,
    String position,
    Instant hireDate,
    Status status,
    String memo
) {
}
