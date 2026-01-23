package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;

import java.time.Instant;
import java.util.Map;

public record EmployeeHistoryResponse(
    Long id,
    EmployeeHistoryType type,
    String memo,
    String ip_address,
    Instant created_at,
    Map<String, Object> changed_detail,
    String employee_name,
    String employee_number
){
}
