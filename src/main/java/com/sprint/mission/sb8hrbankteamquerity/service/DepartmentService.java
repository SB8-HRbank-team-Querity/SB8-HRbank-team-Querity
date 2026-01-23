package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentCreateRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentUpdateRequest;
import java.util.List;

public interface DepartmentService {
    // 부서 생성
    DepartmentDto create(DepartmentCreateRequest departmentCreateRequest);

    // 부서 수정
    DepartmentDto update(Long departmentId, DepartmentUpdateRequest departmentUpdateRequest);

    // 부서 단건 조회
    DepartmentDto find(Long departmentId);

    // 부서 목록 조회
    List<DepartmentDto> findAll();

    // 부서 삭제
    void delete(Long departmentId);
}
