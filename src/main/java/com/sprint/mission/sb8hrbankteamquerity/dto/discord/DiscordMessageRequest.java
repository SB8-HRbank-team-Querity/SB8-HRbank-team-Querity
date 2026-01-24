package com.sprint.mission.sb8hrbankteamquerity.dto.discord;

import java.util.List;

public record DiscordMessageRequest(
    String content, // 메시지 본문 (일반 텍스트, 멘션 등)
    List<DiscordEmbedRequest> embeds // 임베드 목록 (박스 형태의 상세 정보)
) {
}
