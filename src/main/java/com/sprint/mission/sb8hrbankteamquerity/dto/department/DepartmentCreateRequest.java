package com.sprint.mission.sb8hrbankteamquerity.dto.department;

import java.util.Date;

public record DepartmentCreateRequest(
    String name,
    String description,
    Date establishedDate
) {

}
