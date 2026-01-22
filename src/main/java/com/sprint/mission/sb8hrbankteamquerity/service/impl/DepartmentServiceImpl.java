package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentCreateRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentUpdateRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import com.sprint.mission.sb8hrbankteamquerity.mapper.DepartmentMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.DepartmentRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.DepartmentService;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentDto create(DepartmentCreateRequest departmentCreateRequest) {
        String name = departmentCreateRequest.name();
        String description = departmentCreateRequest.description();
        Date establishedDate = departmentCreateRequest.establishedDate();

        // 부서 이름이 중복일 경우
        if (departmentRepository.existsByName(name)){
            throw new IllegalArgumentException("Department already exists");
        }

        Department department = new Department(name, description, establishedDate);
        departmentRepository.save(department);

        return departmentMapper.toDto(department);
    }

    @Override
    public DepartmentDto update(Long departmentId,
        DepartmentUpdateRequest departmentUpdateRequest) {

        return null;
    }

    @Override
    public DepartmentDto find(Long departmentId) {
        return null;
    }

    @Override
    public List<DepartmentDto> findAll() {
        return List.of();
    }

    @Override
    public void delete(Long departmentId) {

    }
}
