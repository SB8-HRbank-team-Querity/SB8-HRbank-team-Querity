package com.sprint.mission.sb8hrbankteamquerity.dto.department;

import java.time.LocalDate;

public record DepartmentDto(
    Long id,
    String name,
    String description,
    LocalDate establishedDate,
    Integer employeeCount) {

}
