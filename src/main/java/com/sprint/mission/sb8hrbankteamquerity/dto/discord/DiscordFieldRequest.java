package com.sprint.mission.sb8hrbankteamquerity.dto.discord;

/*
* 디스코드 임베드(Embed) 내부에 들어가는 키-값 형태의 필드입니다.
* 예:
* | Path : /api/files | Time : 2024-01-01 | (inline = true)
* */
public record DiscordFieldRequest(
    String name,   // 필드 제목 (예: Path, Time)
    String value,  // 필드 내용 (예: /api/files, 2024-01-01...)
    
    /*
    * true: 다른 필드와 같은 라인에 가로로 배치됩니다. (공간 절약)
    * false: 한 줄을 통째로 차지하며 세로로 배치됩니다. (내용이 길 때 추천)
    * */
    boolean inline
) {
}
