package com.sprint.mission.sb8hrbankteamquerity.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements ErrorCode {

    FILE_NOT_FOUND(5001, "NOT_FOUND", HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    FILE_UPLOAD_FAILED(5002, "UPLOAD_FAILED", HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_DOWNLOAD_FAILED(5003, "DOWNLOAD_FAILED", HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드에 실패했습니다."),
    INVALID_FILE_NAME(5004, "INVALID_NAME", HttpStatus.BAD_REQUEST, "잘못된 파일명입니다."),
    INTERNAL_SERVER_ERROR(5005, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "파일 시스템 오류입니다");

    private final int numeric;
    private final String errorKey;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getDomain() {
        return "FILE";
    }

    @Override
    public String getCode() {
        return getDomain() + "-" + getErrorKey();
    }


    }
