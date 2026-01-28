package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.enums.EmployeeHistoryType;

import java.util.List;

public record EmployeeHistorySaveRequest(
    EmployeeHistoryType type,
    String memo,
    String ipAddress,
    Long profileImageId,
    List<DiffDto> changed_detail,
    String employeeName,
    String employeeNumber
) {
}
