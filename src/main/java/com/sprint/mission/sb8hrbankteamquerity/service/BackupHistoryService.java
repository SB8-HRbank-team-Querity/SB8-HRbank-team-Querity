package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.CursorPageResponseBackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.enums.BackupHistoryStatus;

public interface BackupHistoryService {

    CursorPageResponseBackupHistoryDto findAll(BackupHistoryPageRequest request);

    BackupHistoryDto create(String worker);

    BackupHistoryDto findLatestByStatus(BackupHistoryStatus status);

    int updateInProgressToFailed();
}
