package com.sprint.mission.sb8hrbankteamquerity.dto.department;

public record DepartmentPageRequest(
    String nameOrDescription,
    Long idAfter,
    String cursor,
    int size,
    String sortField,
    String sortDirection
) {

}
