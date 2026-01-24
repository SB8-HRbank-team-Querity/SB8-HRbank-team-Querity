package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;

import java.net.UnknownHostException;
import java.util.List;

public interface BackupHistoryService {

    BackupHistoryDto create(String worker) throws UnknownHostException;

    List<BackupHistoryDto> findAll();

    List<BackupHistoryDto> findLatestByStatus(BackupHistoryStatus status);
}
