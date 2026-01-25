package com.sprint.mission.sb8hrbankteamquerity.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BackupHistoryErrorCode implements ErrorCode {

    BACKUP_ALREADY_IN_PROGRESS(6001, "ALREADY_IN_PROGRESS", HttpStatus.CONFLICT, "이미 진행 중인 백업이 있습니다.");

    private final int numeric;
    private final String errorKey;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getDomain() {
        return "BACKUP";
    }

    @Override
    public String getCode() {
        return getDomain() + "-" + getErrorKey();
    }
}
