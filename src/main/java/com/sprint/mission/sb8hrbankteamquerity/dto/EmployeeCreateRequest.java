package com.sprint.mission.sb8hrbankteamquerity.dto;

import java.time.Instant;

public record EmployeeCreateRequest(
    String name,
    String email,
    int departmentId,
    String position,
    Instant hireDate,
    String memo
) {
}
