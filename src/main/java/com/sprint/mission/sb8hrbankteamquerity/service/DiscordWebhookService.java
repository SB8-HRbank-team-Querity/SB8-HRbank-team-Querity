package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.exception.ErrorCode;

public interface DiscordWebhookService {

    void sendAlert(ErrorCode errorCode, String errorMessage, String path);

}
