package com.sprint.mission.sb8hrbankteamquerity.dto.BuckupHistory;

public record BackupHistoryDto(
    int id,
    String worker,
    String startedAt,
    String endedAt,
    String status,
    Long fileId

) {
}
