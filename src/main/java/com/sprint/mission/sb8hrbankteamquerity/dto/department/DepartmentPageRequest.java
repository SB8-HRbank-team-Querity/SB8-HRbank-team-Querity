package com.sprint.mission.sb8hrbankteamquerity.dto.department;

public record DepartmentPageRequest(
    String nameOrDescription,
    Long idAfter,
    String cursor,
    Integer size,
    String sortField,
    String sortDirection
) {

}
