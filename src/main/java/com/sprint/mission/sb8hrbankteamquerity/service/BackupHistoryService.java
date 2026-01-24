package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;

import java.util.List;

public interface BackupHistoryService {

    BackupHistoryDto create(String worker);

    List<BackupHistoryDto> findAll();

    BackupHistoryDto findLatestByStatus(BackupHistoryStatus status);
}
