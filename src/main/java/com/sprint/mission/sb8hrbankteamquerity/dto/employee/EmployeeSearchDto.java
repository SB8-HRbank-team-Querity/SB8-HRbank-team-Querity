package com.sprint.mission.sb8hrbankteamquerity.dto.employee;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;

public record EmployeeSearchDto(
    String nameOrEmail,
    String employeeNumber,
    String departmentName,
    String position,
    String hireDateFrom,
    String hireDateTo,
    EmployeeStatus status,
    Long idAfter,
    String cursor,
    Integer size,
    String sortField,
    String sortDirection
) {
}