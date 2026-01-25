package com.sprint.mission.sb8hrbankteamquerity.dto.department;

import java.time.Instant;

public record DepartmentUpdateRequest(
    String name,
    String description,
    Instant establishedDate
) {

}
