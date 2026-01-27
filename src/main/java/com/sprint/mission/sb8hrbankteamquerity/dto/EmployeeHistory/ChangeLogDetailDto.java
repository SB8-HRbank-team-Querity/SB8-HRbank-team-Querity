package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;

import java.time.Instant;
import java.util.List;

public record ChangeLogDetailDto(
    Long  id,
    EmployeeHistoryType type,
    String employeeNumber,
    String memo,
    String ipAddress,
    Instant at,
    String employeeName,
    Long  profileImageId,
    List<DiffDto> diffs
) {
}
