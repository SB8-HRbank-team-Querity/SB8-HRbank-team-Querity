package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import com.sprint.mission.sb8hrbankteamquerity.entity.sortType;

import java.time.Instant;

public record EmployeeHistoryFilter(
    EmployeeHistoryType type,
    String employeeNumber,
    String memo,
    String ipAddress,
    Instant atFrom,
    Instant atTo,
    String sortField, // 기본은 ip로
    sortType direction, // 기본은 desc로
    Instant cursor,
    Integer size,
    Long idAfter
) {
}
