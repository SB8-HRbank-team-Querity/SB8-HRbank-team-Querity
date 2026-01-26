package com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import jakarta.validation.constraints.Min;

import java.time.Instant;

public record BackupHistoryPageRequest(
    // 작업자
    String worker,
    // 상태 (IN_PROGRESS, COMPLETED, FAILED)
    BackupHistoryStatus status,
    // 시작 시간(부터)
    Instant startedAtFrom,
    // 시작 시간(까지)
    Instant startedAtTo,
    // 이전 페이지 마지막 요소 ID
    Integer idAfter,
    // 커서 (이전 페이지의 마지막 ID)
    String cursor,
    // 페이지 크기
    @Min(value = 1, message = "최소 1개 이상")
    Integer size,
    // 정렬 필드 (startedAt, endedAt, status)
    String sortField,
    // 정렬 방향 (ASC, DESC)
    String sortDirection
) {
    public BackupHistoryPageRequest {
        if (size == null) {
            size = 10;
        }
    }
}
