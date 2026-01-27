package com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard;

public record EmployeeDistributionDto(
    String groupKey,
    int count,
    double percentage
) {
}
