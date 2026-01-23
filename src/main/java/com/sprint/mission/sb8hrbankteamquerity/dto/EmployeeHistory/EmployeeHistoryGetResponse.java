package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;

import java.time.Instant;

public record EmployeeHistoryGetResponse(
    Long id,
    EmployeeHistoryType type,
    String memo,
    String ip_address,
    Instant created_at,
    String changed_detail,
    String employee_name,
    String employee_number
){
}
