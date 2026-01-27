package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.controller.docs.DepartmentApi;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.CursorPageResponseDepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentCreateRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentUpdateRequest;
import com.sprint.mission.sb8hrbankteamquerity.exception.BusinessException;
import com.sprint.mission.sb8hrbankteamquerity.exception.DepartmentErrorCode;
import com.sprint.mission.sb8hrbankteamquerity.service.DepartmentService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController implements DepartmentApi {

    private final DepartmentService departmentService;

    // 부서 생성
    @PostMapping
    public ResponseEntity<DepartmentDto> create(
        @RequestBody DepartmentCreateRequest departmentCreateRequest) {
        DepartmentDto departmentDto = departmentService.create(departmentCreateRequest);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(departmentDto);
    }

    // 부서 정보 수정
    @PatchMapping(value = "/{departmentId}")
    public ResponseEntity<DepartmentDto> update(
        @PathVariable Long departmentId,
        @RequestBody DepartmentUpdateRequest departmentUpdateRequest) {
        DepartmentDto departmentDto = departmentService.update(departmentId,
            departmentUpdateRequest);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(departmentDto);
    }

    // 부서 단건 조회
    @GetMapping(value = "/{departmentId}")
    public ResponseEntity<DepartmentDto> find(
        @PathVariable Long departmentId
    ) {
        DepartmentDto departmentDto = departmentService.find(departmentId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(departmentDto);
    }

    // 부서 다건 조회
    @GetMapping
    public ResponseEntity<CursorPageResponseDepartmentDto> findAll(
        DepartmentPageRequest departmentPageRequest) {

        CursorPageResponseDepartmentDto departmentDto = departmentService.findAll(
            departmentPageRequest);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(departmentDto);
    }

    // 부서 삭제
    @DeleteMapping(value = "/{departmentId}")
    public ResponseEntity<Void> delete(
        @PathVariable Long departmentId
    ) {
        departmentService.delete(departmentId);

        // body 본문에 넣을 내용이 없는 경우(즉, 반환값이 void인 경우) build 사용
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile excelFile) {
        try {
            int savedCount = departmentService.importDepartments(excelFile);
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("%d departments have been successfully registered.", savedCount));
        } catch (Exception e) {
            throw new BusinessException(DepartmentErrorCode.INVALID_EXCEL_FILE);
        }
    }

    @GetMapping(value = "/export")
    public ResponseEntity<byte[]> exportExcel(){
        try {
            byte[] fileContent = departmentService.exportDepartments();

            // LocalDateTime을 안전한 문자열로 변환
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
            String fileName = "departments_" + timestamp + ".xlsx";

            return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(fileContent);
        } catch (Exception e) {
            throw new BusinessException(DepartmentErrorCode.EXCEL_EXPORT_ERROR);
        }
    }
}
