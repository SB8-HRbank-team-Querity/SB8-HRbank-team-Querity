package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;

import java.util.List;

public record EmployeeHistorySaveRequest(
    EmployeeHistoryType type,
    String memo,
    String ipAddress,
    List<DiffDto> changed_detail,
    String employeeName,
    String employeeNumber
) {
}
