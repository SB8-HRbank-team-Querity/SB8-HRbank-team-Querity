package com.sprint.mission.sb8hrbankteamquerity.entity;

import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;

public record EmployeeHistoryDiffRequest(
    EmployeeDto oldDto,
    EmployeeDto newDto
) {}