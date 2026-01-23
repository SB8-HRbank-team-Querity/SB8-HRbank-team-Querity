package com.sprint.mission.sb8hrbankteamquerity.dto.error;

import com.sprint.mission.sb8hrbankteamquerity.exception.ErrorCode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

// 에러 응답용 DTO
public record ErrorResponse(
    Instant timestamp,
    String traceId,
    String path,
    String code,
    String errorKey,
    int numeric,
    String title,
    String message,
    List<Detail> details,
    String hint
) {
    public record Detail(String field, String issue, Object rejected) {}

    // 1. 기본형 (BusinessException 등 일반적인 에러)
    public static ErrorResponse of(ErrorCode code, String message, String path) {
        return new ErrorResponse(
            Instant.now(),
            UUID.randomUUID().toString().substring(0, 8),
            path,
            code.getCode(),
            code.getErrorKey(),
            code.getNumeric(),
            code.getHttpStatus().getReasonPhrase(),
            message,
            null,
            null
        );
    }

    // 2. Details 포함형 (유효성 검사 실패 등 상세 정보가 필요한 경우)
    public static ErrorResponse of(ErrorCode code, String message, String path, List<Detail> details) {
        return new ErrorResponse(
            Instant.now(),
            UUID.randomUUID().toString().substring(0, 8),
            path,
            code.getCode(),
            code.getErrorKey(),
            code.getNumeric(),
            code.getHttpStatus().getReasonPhrase(),
            message,
            details,
            null
        );
    }

    // 3. Hint 포함형 (사용자에게 힌트를 주고 싶은 경우)
    public static ErrorResponse of(ErrorCode code, String message, String path, String hint) {
        return new ErrorResponse(
            Instant.now(),
            UUID.randomUUID().toString().substring(0, 8),
            path,
            code.getCode(),
            code.getErrorKey(),
            code.getNumeric(),
            code.getHttpStatus().getReasonPhrase(),
            message,
            null,
            hint
        );
    }
}
