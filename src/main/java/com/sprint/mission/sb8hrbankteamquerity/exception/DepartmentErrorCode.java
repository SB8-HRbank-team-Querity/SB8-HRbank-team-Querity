package com.sprint.mission.sb8hrbankteamquerity.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DepartmentErrorCode implements ErrorCode {
    DEPT_NOT_FOUND(1001, "NOT_FOUND", HttpStatus.NOT_FOUND, "해당 부서를 찾을 수 없습니다."),
    DUPLICATE_DEPT_NAME(1002, "DUPLICATE_NAME", HttpStatus.CONFLICT, "이미 존재하는 부서 이름입니다."),
    DEPT_NOT_EMPTY(1003, "NOT_EMPTY", HttpStatus.CONFLICT, "해당 부서에 소속된 직원이 있어 삭제할 수 없습니다.");

    private final int numeric;
    private final String errorKey;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getDomain() {
        return "DEPT";
    }

    @Override
    public String getCode() {
        return getDomain() + "-" + getErrorKey();
    }
}
