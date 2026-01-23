package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.department.CursorPageResponseDepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentCreateRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentUpdateRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import com.sprint.mission.sb8hrbankteamquerity.mapper.DepartmentMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.DepartmentRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.DepartmentService;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
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

        // 부서가 존재하지 않을 경우 오류 처리
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        String newName = departmentUpdateRequest.newName();
        String newDescription = departmentUpdateRequest.newDescription();
        Date newEstablishedDate = departmentUpdateRequest.newEstablishedDate();

        // 바꾸려는 부서의 이름이 이미 존재하는 경우 오류 처리
        if (departmentRepository.existsByName(newName)){
            throw new IllegalArgumentException("Department already exists");
        }

        department.update(newName, newDescription, newEstablishedDate);
        departmentRepository.save(department);

        return departmentMapper.toDto(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto find(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        return departmentMapper.toDto(department);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDepartmentDto findAll(DepartmentPageRequest departmentPageRequest) {
        // 데이터 조회
        Sort sort = departmentPageRequest.sortDirection().equalsIgnoreCase("desc")
            ? Sort.by(departmentPageRequest.sortField()).descending()
            : Sort.by(departmentPageRequest.sortField()).ascending();

        // 정렬값이 같을 경우 id 오름차순 정렬
        sort = sort.and(Sort.by("id").ascending());

        Pageable pageable = PageRequest.of(0, departmentPageRequest.size() + 1, sort);

        List<Department> departments = departmentRepository.findAllByCursor(
            departmentPageRequest.nameOrDescription(),
            departmentPageRequest.idAfter(),
            pageable
        );

        // 다음 페이지 여부 확인
        // 조회된 데이터의 size가 한 페이지의 데이터 size보다 크다면 다음 데이터가 더 있다는 의미
        boolean hasNext = departments.size() > departmentPageRequest.size();

        // 응답에 포함할 실제 데이터 계산
        // departments로 조회한 부서 목록은 17개이지만,
        // page 요청의 size가 15개라면
        // 2개는 제외하고 15개만 반환!
        List<Department> content =
            hasNext ? departments.subList(0, departmentPageRequest.size()) : departments;

        // 다음 페이지를 위한 정보
        Department lastItem =
            (hasNext && !content.isEmpty()) ? content.get(content.size() - 1) : null;
        Long nextIdAfter = (lastItem != null) ? lastItem.getId() : null;
        String nextCursor = (lastItem != null) ? String.valueOf(lastItem.getId()) : null;

        // 전체 개수 조회
        long totalElements = departmentRepository.countByNameOrDescription(
            departmentPageRequest.nameOrDescription());

        // 변환 및 반한
        return new CursorPageResponseDepartmentDto(
            content.stream().map(departmentMapper::toDto).toList(),
            nextCursor,
            nextIdAfter,
            departmentPageRequest.size(),
            totalElements,
            hasNext
        );
    }

    @Override
    public void delete(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        departmentRepository.delete(department);
    }
}
