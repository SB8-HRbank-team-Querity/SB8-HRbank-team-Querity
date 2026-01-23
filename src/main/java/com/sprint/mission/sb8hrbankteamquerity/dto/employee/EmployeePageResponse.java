package com.sprint.mission.sb8hrbankteamquerity.dto.employee;

import java.util.List;

public record EmployeePageResponse(
    List<EmployeeDto> content,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    Integer totalElements,
    boolean hasNext
) {
}
