package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.enums.EmployeeHistoryType;
import com.sprint.mission.sb8hrbankteamquerity.entity.enums.sortType;

import java.time.Instant;

public record EmployeeHistoryFilter(
    EmployeeHistoryType type,
    String employeeNumber,
    String memo,
    String ipAddress,
    Instant atFrom,
    Instant atTo,
    String sortField, // 기본은 ip로
    sortType sortDirection, // 기본은 desc로
    Instant cursor,
    Integer size,
    Long idAfter
) {
}
