package com.sprint.mission.sb8hrbankteamquerity.exception;

import com.sprint.mission.sb8hrbankteamquerity.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {

        ErrorCode code = e.getErrorCode();

        // 서버 로그 남기기
        log.error("비즈니스 예외 발생 : code={}, message={}, path={}", code.getCode(), code.getMessage(), request.getRequestURI());

        // 응답 생성
        ErrorResponse response;
        if (e.getHint() != null) {
            response = ErrorResponse.of(code, e.getMessage(), request.getRequestURI(), e.getHint());
        } else {
            response = ErrorResponse.of(code, e.getMessage(), request.getRequestURI());
        }

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }

    // @Valid 유효성 검사 실패 시 실행
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorCode code = GlobalErrorCode.INVALID_INPUT;

        // 에러 필드 정보 추출
        List<ErrorResponse.Detail> details = e.getBindingResult().getFieldErrors().stream()
            .map(error -> new ErrorResponse.Detail(
                error.getField(),
                error.getDefaultMessage(),
                error.getRejectedValue()
            ))
            .toList();
        
        log.error("유효성 검사 실패 : code={}, message={}, path={}, details={}", code.getCode(), code.getMessage(), request.getRequestURI(), details);

        ErrorResponse response = ErrorResponse.of(code, code.getMessage(), request.getRequestURI(), details);

        return ResponseEntity.status(code.getHttpStatus()).body(response);
    }

}
