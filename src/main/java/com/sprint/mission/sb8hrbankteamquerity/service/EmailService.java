package com.sprint.mission.sb8hrbankteamquerity.service;

public interface EmailService {
    void sendBackupStatusEmail(Long backupId, boolean isSuccess, String errorMessage);
}
