package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistory;

import java.net.UnknownHostException;
import java.util.List;

public interface BackupHistoryService {

    BackupHistoryDto create() throws UnknownHostException;

    List<BackupHistoryDto> findAll();

    List<BackupHistoryDto> findLatestByStatus();
}
