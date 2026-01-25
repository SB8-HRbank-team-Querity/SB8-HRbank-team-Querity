package com.sprint.mission.sb8hrbankteamquerity.controller.docs;

import com.sprint.mission.sb8hrbankteamquerity.dto.department.CursorPageResponseDepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentCreateRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentUpdateRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;

@Tag(name = "부서 관리", description = "부서 관리 API")
public interface DepartmentApi {

    @Operation(summary = "부서 등록", description = "새로운 부서를 등록합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", description = "등록 성공",
            content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
        @ApiResponse(
            responseCode = "400", description = "잘못된 요청 또는 중복된 이름",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<DepartmentDto> create(
        @ParameterObject DepartmentCreateRequest departmentCreateRequest
    );

    @Operation(summary = "부서 수정", description = "부서 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
        @ApiResponse(
            responseCode = "400", description = "잘못된 요청 또는 중복된 이름",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404", description = "부서를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<DepartmentDto> update(
        @Parameter(description = "부서 ID") Long departmentId,
        @ParameterObject DepartmentUpdateRequest departmentUpdateRequest
    );

    @Operation(summary = "부서 상세 조회", description = "부서 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
        @ApiResponse(
            responseCode = "404", description = "부서를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<DepartmentDto> find(
        @Parameter(description = "부서 ID") Long departmentId);

    @Operation(summary = "부서 목록 조회", description = "부서 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", description = "목록 조회 성공",
            content = @Content(schema = @Schema(implementation = CursorPageResponseDepartmentDto.class))),
        @ApiResponse(
            responseCode = "400", description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "500", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<CursorPageResponseDepartmentDto> findAll(
        @ParameterObject DepartmentPageRequest departmentPageRequest
    );

    @Operation(summary = "부서 삭제", description = "부서를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", description = "삭제 성공",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "400", description = "부서를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404", description = "서버 오류",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> delete(
        @Parameter(description = "부서 ID") Long departmentId);
}
