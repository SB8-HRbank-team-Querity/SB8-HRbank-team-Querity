package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.controller.docs.DepartmentApi;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.CursorPageResponseDepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentCreateRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentUpdateRequest;
import com.sprint.mission.sb8hrbankteamquerity.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<CursorPageResponseDepartmentDto> findAll(DepartmentPageRequest departmentPageRequest) {

        CursorPageResponseDepartmentDto departmentDto = departmentService.findAll(departmentPageRequest);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(departmentDto);
    }

    // 부서 삭제
    @DeleteMapping(value = "/{departmentId}")
    public ResponseEntity<Void> delete(
        @PathVariable Long departmentId
    ){
        departmentService.delete(departmentId);

        // body 본문에 넣을 내용이 없는 경우(즉, 반환값이 void인 경우) build 사용
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
