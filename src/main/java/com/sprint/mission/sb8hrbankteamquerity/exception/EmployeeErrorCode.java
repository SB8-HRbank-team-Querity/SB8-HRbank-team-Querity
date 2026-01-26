package com.sprint.mission.sb8hrbankteamquerity.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmployeeErrorCode implements ErrorCode {

    EMP_NOT_FOUND(2001,"NOT_FOUND",HttpStatus.NOT_FOUND,"해당 직원을 찾을 수 없습니다."),
    EMP_DUPLICATE_EMAIL(2002,"DUPLICATE_EMAIL",HttpStatus.CONFLICT,"이미 사용 중인 이메일입니다.");

    private final int numeric;
    private final String errorKey;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getDomain() {
        return "EMP";
    }

    @Override
    public String getCode() {
        return getDomain()+"-"+getErrorKey();
    }

}
