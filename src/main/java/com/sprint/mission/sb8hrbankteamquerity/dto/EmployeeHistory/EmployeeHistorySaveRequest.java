package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;

public record EmployeeHistorySaveRequest(
    EmployeeHistoryType type,
    String memo,
    String ip_address,
    String changed_detail,
    String employee_name,
    String employee_number
) {
}
