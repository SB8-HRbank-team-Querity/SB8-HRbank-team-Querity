package com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public record EmployeeHistoryFilter(
//    @Param("number") String employeeNumber,
//    @Param("memo") String memo,
//    @Param("ip") String ip,
//    @Param("startDate") Instant startDate,
//    @Param("endDate") Instant endDate,
//    @Param("type") EmployeeHistoryType type
    String employeeNumber,
     String memo,
    String ip,
    Instant startDate,
    Instant endDate,
     EmployeeHistoryType type
) {
}
