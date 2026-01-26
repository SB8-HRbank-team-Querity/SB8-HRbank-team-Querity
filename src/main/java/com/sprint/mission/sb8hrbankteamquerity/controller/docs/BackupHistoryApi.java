package com.sprint.mission.sb8hrbankteamquerity.controller.docs;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.CursorPageResponseBackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.error.ErrorResponse;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "데이터 백업 관리", description = "데이터 백업 관리 API")
public interface BackupHistoryApi {

    @Operation(summary = "데이터 백업 목록 조회", description = "데이터 백업 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BackupHistoryPageRequest.class))
        ),
        @ApiResponse(
            responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", description = "지원하지 않는 정렬 필드",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<CursorPageResponseBackupHistoryDto> findAll(
        @ParameterObject BackupHistoryPageRequest request
    );

    @Operation(summary = "데이터 백업 생성", description = "데이터 백업을 생성합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", description = "백업 생성 성공",
            content = @Content(schema = @Schema(implementation = CursorPageResponseBackupHistoryDto.class))
        ),
        @ApiResponse(
            responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409", description = "이미 진행 중인 백업이 있음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<BackupHistoryDto> create();

    @Operation(summary = "최근 백업 정보 조회", description = "지정된 상태의 가장 최근 백업 정보를 조회합니다. 상태를 저장하지 않으면 성공적으로 완료된(COMPLETE) 백업을 반환합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BackupHistoryDto.class))
        ),
        @ApiResponse(
            responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", description = "유효하지 않은 상태값",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    ResponseEntity<BackupHistoryDto> findLatestByStatus(
        @Parameter(
            description = "백업 상태 (COMPLETED, FAILED, IN_PROGRESS, 기본값: COMPLETED)",
            example = "COMPLETED"
        ) BackupHistoryStatus status
    );
}
