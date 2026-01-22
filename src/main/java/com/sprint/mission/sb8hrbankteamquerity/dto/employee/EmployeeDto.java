package com.sprint.mission.sb8hrbankteamquerity.dto.employee;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;
import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;

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
    EmployeeStatus status,
    FileMetaDto profileImageId
) {
}
