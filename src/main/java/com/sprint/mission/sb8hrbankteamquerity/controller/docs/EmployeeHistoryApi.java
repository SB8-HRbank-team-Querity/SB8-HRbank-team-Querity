package com.sprint.mission.sb8hrbankteamquerity.controller.docs;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryFilter;
import com.sprint.mission.sb8hrbankteamquerity.dto.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface EmployeeHistoryApi {
    @GetMapping
    @Operation(
        summary = "수정 이력 조회",
        description = "수정 이력 페이지에 출력될 정보를 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = ChangeLogDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 지원하지 않는 정렬 필드",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<List<ChangeLogDto>> getEmployeeHistory(
        @ModelAttribute EmployeeHistoryFilter filter
    );

    @GetMapping("/{employeeHistoryId}")
    @Operation(
        summary = "수정 이력 상세 조회",
        description = "조회 이력의 id로 어떤 이력이 바뀐 것인지 상세한 정보를 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "업로드 성공",
            content = @Content(schema = @Schema(implementation = ChangeLogDetailDto.class))),
        @ApiResponse(responseCode = "404", description = "이력을 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<ChangeLogDetailDto> getEmployeeHistoryById(
        @PathVariable Long employeeHistoryId
    );


    @GetMapping("/count")
    @Operation(
        summary = "수정 횟수 조회",
        description = "날짜를 입력 받으면 해당 기간/날짜에 이루어진 수정의 횟수를 가져옵니다. " +
            "\t\n 입력이 없다면 최근 7일간의 기록을 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = Long.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 날짜 범위",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Long> getEmployeeHistoryCount(
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate
    );
}
