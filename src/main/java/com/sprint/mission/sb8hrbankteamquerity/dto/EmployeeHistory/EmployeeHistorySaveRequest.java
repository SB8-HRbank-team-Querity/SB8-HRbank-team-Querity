package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;

import java.util.Map;

public record EmployeeHistorySaveRequest(
    EmployeeHistoryType type,
    String memo,
    Map<String, Object> changed_detail,
    String employee_name,
    String employee_number
) {
}
