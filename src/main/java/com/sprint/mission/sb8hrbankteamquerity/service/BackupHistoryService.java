package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.CursorPageResponseBackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;

public interface BackupHistoryService {

    BackupHistoryDto create(String worker);

    CursorPageResponseBackupHistoryDto findAll(BackupHistoryPageRequest request);

    BackupHistoryDto findLatestByStatus(BackupHistoryStatus status);
}
