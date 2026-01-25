package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import com.sprint.mission.sb8hrbankteamquerity.entity.sortType;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;

public record EmployeeHistoryFilter(
    EmployeeHistoryType type,
    String employeeNumber,
    String memo,
    String ipAddress,
    String atFrom,
    String atTo,
    String sortField, // 기본은 id로
    sortType direction, // 기본은 desc로
    Long idAfter
) {
}
