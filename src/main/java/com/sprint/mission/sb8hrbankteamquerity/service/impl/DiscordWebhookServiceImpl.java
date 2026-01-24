package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.exception.ErrorCode;
import com.sprint.mission.sb8hrbankteamquerity.service.DiscordWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordWebhookServiceImpl implements DiscordWebhookService {

    private static final String WEBHOOK_URL = "${discord.webhook.url}";

    @Value(WEBHOOK_URL)
    private String webhookUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendAlert(ErrorCode errorCode, String errorMessage, String path) {

    }
}
