package com.sprint.mission.sb8hrbankteamquerity.dto.department;

import java.time.Instant;

public record DepartmentDto(
    Long id,
    String name,
    String description,
    Instant establishedDate,
    Integer employeeCount) {

}
