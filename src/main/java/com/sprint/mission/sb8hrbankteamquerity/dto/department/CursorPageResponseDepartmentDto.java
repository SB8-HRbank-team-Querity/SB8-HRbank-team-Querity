package com.sprint.mission.sb8hrbankteamquerity.dto.department;

import java.util.List;

public record CursorPageResponseDepartmentDto(
    List<DepartmentDto> content,
    String nextCursor,
    Long nextIdAfter, // integer 64(null 값이 필요할 때는 Wrapper 클래스 사용)
    int size,
    long totalElements, // integer 64(보통 0 이상, null이 아님)
    boolean hasNext
) {

}
