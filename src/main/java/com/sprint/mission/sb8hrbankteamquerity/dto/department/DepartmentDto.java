package com.sprint.mission.sb8hrbankteamquerity.dto.department;

import java.util.Date;

public record DepartmentDto(
    Long id,
    String name,
    String description,
    Date establishedDate,
    Integer employeeCount) {

}
