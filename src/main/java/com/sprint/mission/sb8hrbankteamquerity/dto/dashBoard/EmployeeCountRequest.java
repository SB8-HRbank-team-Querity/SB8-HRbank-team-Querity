package com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;

import java.time.LocalDate;

public record EmployeeCountRequest(
    EmployeeStatus status,
    LocalDate fromDate,
    LocalDate toDate
) {

}
