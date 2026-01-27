package com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory;

import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import lombok.Builder;
import org.springframework.data.domain.Sort;

import java.time.Instant;

@Builder
public record BackupHistorySearchCondition(
    // 커서 정보
    Long cursorId,
    String cursorValue,

    // 필터링 정보
    String worker,
    BackupHistoryStatus statusFilter,
    Instant startedAtFrom,
    Instant startedAtTo,

    // 정렬 정보
    String sortField,
    Sort.Direction direction
) {
}
