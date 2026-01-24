package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;

import java.time.Instant;

public record ChangeLogDto(
    Long id,
    EmployeeHistoryType type,
    String employeeNumber,
    String memo,
    String ipAddress,
    Instant at
) {
}
