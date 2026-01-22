package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import java.time.Instant;

public record EmployeeHistoryResponse(
    Long id,
    EmployeeHistoryType type,
    String memo,
    String ip_address,
    Instant created_at,
    String changed_detail,
    String employee_name,
    String employee_number
) {
}

// 이미시로 만드넉 대량의 수정 필요