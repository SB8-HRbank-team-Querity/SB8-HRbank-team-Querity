package com.sprint.mission.sb8hrbankteamquerity.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmployeeHistoryErrorCode implements ErrorCode {

    EMP_HIST_NOT_FOUND(3001,"NOT_FOUND",HttpStatus.NOT_FOUND,"찾을 수 없는 이력입니다.");

    private final int numeric;
    private final String errorKey;
    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String getDomain() {
        return "EMP_HIST";
    }

    @Override
    public String getCode() {
        return getDomain() + "-" + getErrorKey();
    }

    @Override
    public boolean shouldAlert() {
        return this == EMP_HIST_NOT_FOUND;
    }
}
