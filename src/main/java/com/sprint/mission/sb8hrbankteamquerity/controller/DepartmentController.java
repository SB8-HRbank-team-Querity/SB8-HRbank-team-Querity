package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentCreateRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentUpdateRequest;
import com.sprint.mission.sb8hrbankteamquerity.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {

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
        DepartmentDto departmentDto = departmentService.update(departmentId, departmentUpdateRequest);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(departmentDto);
    }
}
