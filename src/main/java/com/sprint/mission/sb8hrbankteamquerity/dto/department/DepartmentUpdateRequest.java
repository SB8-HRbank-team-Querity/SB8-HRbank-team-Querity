package com.sprint.mission.sb8hrbankteamquerity.dto.department;

import java.time.LocalDate;

public record DepartmentUpdateRequest(
    String name,
    String description,
    LocalDate establishedDate
) {

}
