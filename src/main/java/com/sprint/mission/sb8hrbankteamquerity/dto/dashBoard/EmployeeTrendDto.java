package com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard;

import java.time.LocalDate;

public record EmployeeTrendDto(
    LocalDate date,
    long count,
    long change,
    double changeRate
) {
}
