package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.discord.DiscordEmbedRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.discord.DiscordFieldRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.discord.DiscordMessageRequest;
import com.sprint.mission.sb8hrbankteamquerity.exception.ErrorCode;
import com.sprint.mission.sb8hrbankteamquerity.service.DiscordWebhookService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordWebhookServiceImpl implements DiscordWebhookService {

    private static final String WEBHOOK_URL = "${discord.webhook.url}";

    // 상수 정의
    private static final int COLOR_RED = 16711680;
    private static final String FIELD_PATH = "Path";
    private static final String FIELD_TIME = "Time";
    private static final String TITLE_PREFIX = "\uD83D\uDEA8 Error Code: ";
    private static final String DEFAULT_MENTION = "@here";
    private static final String EVERYONE = "@everyone";
    private static final String MESSAGE_TEMPLATE = "\uD83D\uDEA8 **%s**님, %s 도메인 에러가 발생했습니다!";

    @Value(WEBHOOK_URL)
    private String webhookUrl;

    private static final Map<String, String> DOMAIN_OWNERS = Map.of(
      "DEPT", "966326166292148244",          // 박하민
        "EMP", "1060423145204494416",            // 이예은
        "EMP_HIST", "518432642597912596",        // 황진서
        "BACKUP", "1436214390155378718",         // 이재준
        "FILE", "344097279885574144",            // 홍성휘
        "GLOBAL", "@everyone"                        // 전부 다!
    );

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    @Override
    public void sendAlert(ErrorCode errorCode, String errorMessage, String path) {

        try {
            // 멘션 생성
            String ownerId = DOMAIN_OWNERS.getOrDefault(errorCode.getDomain(), "");
            String mention;

            // 누구를 멘션할지 결정
            if ("everyone".equals(ownerId)) {
                mention = EVERYONE;
            } else if (ownerId.isEmpty()) {
                mention = DEFAULT_MENTION;
            } else {
                mention = "<@" + ownerId + ">";
            }

            // 메시지 본문 생성하기
            String content = String.format(MESSAGE_TEMPLATE, mention, errorCode.getDomain());

            // 필드 생성하기
            List<DiscordFieldRequest> fields = List.of(
                new DiscordFieldRequest(FIELD_PATH, path, true),
                new DiscordFieldRequest(FIELD_TIME, LocalDateTime.now().toString(), true)
            );

            // 임베드 생성하기
            DiscordEmbedRequest embed = new DiscordEmbedRequest(
                TITLE_PREFIX + errorCode.getCode(),
                errorMessage,
                COLOR_RED, // 빨간색
                fields
            );

            // 메시지 생성하기
            DiscordMessageRequest message = new DiscordMessageRequest(
                content,
                List.of(embed)
            );

            // 전송하기
            restTemplate.postForEntity(webhookUrl, message, String.class);

        } catch (Exception e) {
            log.error("디스코드 알림 전송 실패", e);
        }

    }
}
