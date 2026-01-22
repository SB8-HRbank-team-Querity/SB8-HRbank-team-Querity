package com.sprint.mission.sb8hrbankteamquerity.dto;

import com.sprint.mission.sb8hrbankteamquerity.entity.Employee.Status;

import java.time.Instant;

public record EmployeeDto(
    Long id,
    String name,
    String email,
    String employeeNumber,
    int departmentId,
    String departmentName,
    String position,
    Instant hireDate,
    Status status,
    FileMeta profileImageId
) {
}
