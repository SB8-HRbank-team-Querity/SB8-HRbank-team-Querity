package com.sprint.mission.sb8hrbankteamquerity.dto.fileMeta;

import java.time.Instant;

public record FileMetaResponse(
    Long id,
    String originName,
    String path,
    String type,
    Long size,
    Instant createdAt,
    Instant updatedAt
) {
}
