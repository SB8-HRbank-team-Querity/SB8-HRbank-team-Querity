package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistorySearchCondition;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BackupHistoryRepositoryCustom {
    List<BackupHistory> findAllByCursor(
        BackupHistorySearchCondition searchCondition,
        Pageable pageable
    );

    Long countByCondition(BackupHistorySearchCondition condition);
}
