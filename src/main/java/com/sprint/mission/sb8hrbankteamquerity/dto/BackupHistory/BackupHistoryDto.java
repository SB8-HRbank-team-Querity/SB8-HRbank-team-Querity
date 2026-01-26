package com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory;

public record BackupHistoryDto(
    Long id,
    String worker,
    String startedAt,
    String endedAt,
    String status,
    Long fileId

) {
}
