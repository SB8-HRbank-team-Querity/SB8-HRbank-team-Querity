package com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard;

import java.time.LocalDate;

public record EmployeeTrendRequest(
    LocalDate from,
    LocalDate to,
    String unit
) {
}
