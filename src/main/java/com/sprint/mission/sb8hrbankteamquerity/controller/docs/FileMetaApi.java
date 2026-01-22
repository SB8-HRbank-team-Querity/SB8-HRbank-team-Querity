package com.sprint.mission.sb8hrbankteamquerity.controller.docs;

import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File 관리", description = "파일 업로드 및 다운로드 API")
public interface FileMetaApi {

    @Operation(summary = "파일 업로드", description = "MultipartFile을 받아 서버 스토리지에 저장하고 메타 정보를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "업로드 성공", content = @Content(schema = @Schema(implementation = FileMeta.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (파일 없음 등)"),
        @ApiResponse(responseCode = "500", description = "서버 저장 실패")
    })
    ResponseEntity<FileMeta> uploadFile(
        @Parameter(description = "업로드할 파일", required = true)
        @RequestParam("file") MultipartFile file
    ) throws IOException;

    @Operation(summary = "파일 다운로드", description = "파일 ID를 받아 해당 파일을 다운로드합니다.")
    @ApiResponses(value = {
        // octet-stream : 이 파일 뭔지 모르겠는데 그냥 0,1로 된 데이터임. 아묻따 걍 다운로드 고고 라는 뜻. -> 모든 종류의 파일 다운로드 가능하게 함
        @ApiResponse(responseCode = "200", description = "다운로드 성공", content = @Content(mediaType = "application/octet-stream")),
        @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음")
    })
    ResponseEntity<Resource> downloadFile(
        @Parameter(description = "파일 ID", required = true, example = "1")
        @PathVariable Long fileId
    );
}
