package com.sprint.mission.sb8hrbankteamquerity.dto.department;

import java.time.LocalDate;

public record DepartmentCreateRequest(
    String name,
    String description,
    LocalDate establishedDate
) {

}
