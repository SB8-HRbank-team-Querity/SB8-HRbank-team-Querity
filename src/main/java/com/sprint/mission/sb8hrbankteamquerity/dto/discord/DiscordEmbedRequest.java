package com.sprint.mission.sb8hrbankteamquerity.dto.discord;

import java.util.List;

/*
* 디스코드 메시지 내부에 포함되는 카드 형태의 박스(Embed)입니다.
* 텍스트만 보내는 것보다 시각적으로 강조할 수 있습니다.
* */
public record DiscordEmbedRequest(
    String title,       // 임베드 상단 제목 (예: Error Code: FILE-001)
    String description, // 임베드 본문 설명 (예: 파일 업로드 중 에러가 발생했습니다.)
    int color,          // 임베드 좌측 띠 색상 (Decimal 값, 예: 빨간색=16711680)
    List<DiscordFieldRequest> fields // 임베드 내부에 들어갈 상세 필드 목록 (Path, Time 등)
) {
}
