package com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory;

import java.util.List;

public record CursorPageResponseBackupHistoryDto(
    List<BackupHistoryDto> content,
    String nextCursor,
    Long nextIdAfter,
    int size,
    Integer totalElements,
    boolean hasNext
) {
}
