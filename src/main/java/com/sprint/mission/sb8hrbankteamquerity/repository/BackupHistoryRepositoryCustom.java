package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

public interface BackupHistoryRepositoryCustom {
    List<BackupHistory> findAllByCursor(
        Long cursorId,
        String cursorValue,
        String worker,
        BackupHistoryStatus statusFilter,
        Instant startedAtFrom,
        Instant startedAtTo,
        String sortField,
        Sort.Direction direction,
        Pageable pageable
    );
}
