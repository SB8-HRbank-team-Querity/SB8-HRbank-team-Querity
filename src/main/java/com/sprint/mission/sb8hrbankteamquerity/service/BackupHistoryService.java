package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupDto;

import java.net.UnknownHostException;
import java.util.List;

public interface BackupHistoryService {

    BackupDto create() throws UnknownHostException;

    List<BackupDto> findAll();

    List<BackupDto> findLatestByStatus();
}
