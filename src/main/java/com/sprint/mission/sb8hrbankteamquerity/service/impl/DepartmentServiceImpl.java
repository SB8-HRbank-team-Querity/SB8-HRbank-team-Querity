package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.department.CursorPageResponseDepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentCreateRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentUpdateRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import com.sprint.mission.sb8hrbankteamquerity.exception.BusinessException;
import com.sprint.mission.sb8hrbankteamquerity.exception.DepartmentErrorCode;
import com.sprint.mission.sb8hrbankteamquerity.mapper.DepartmentMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.DepartmentRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.DepartmentService;
import java.time.Instant;
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
        Instant establishedDate = departmentCreateRequest.establishedDate();

        // 부서 이름이 중복일 경우
        if (departmentRepository.existsByName(name)) {
            throw new BusinessException(DepartmentErrorCode.DUPLICATE_DEPT_NAME);
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
            .orElseThrow(() -> new BusinessException(DepartmentErrorCode.DEPT_NOT_FOUND));

        String newName = departmentUpdateRequest.newName();
        String newDescription = departmentUpdateRequest.newDescription();
        Instant newEstablishedDate = departmentUpdateRequest.newEstablishedDate();

        // 바꾸려는 부서의 이름이 이미 존재하는 경우 오류 처리
        if (!department.getName().equals(newName) && departmentRepository.existsByName(newName)) {
            throw new BusinessException(DepartmentErrorCode.DUPLICATE_DEPT_NAME);
        }

        department.update(newName, newDescription, newEstablishedDate);

        return departmentMapper.toDto(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto find(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new BusinessException(DepartmentErrorCode.DEPT_NOT_FOUND));

        return departmentMapper.toDto(department);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDepartmentDto findAll(DepartmentPageRequest departmentPageRequest) {
        // 데이터 조회
        int size = departmentPageRequest.size() != null && departmentPageRequest.size() > 0
            ? departmentPageRequest.size() : 10;
        String sortField = departmentPageRequest.sortField() == null ? "establishedDate"
            : departmentPageRequest.sortField();
        boolean isAsc = !"desc".equalsIgnoreCase(departmentPageRequest.sortDirection());

        String lastValue = null;
        Instant lastDateValue = null;
        if (departmentPageRequest.idAfter() != null) {
            Department lastDept = departmentRepository.findById(departmentPageRequest.idAfter()).orElse(null);
            if (lastDept != null) {
                if ("name".equals(sortField)) {
                    lastValue = lastDept.getName();
                } else {
                    lastDateValue = lastDept.getEstablishedDate();
                }
            }
        }

        // 정렬값이 같을 경우 id 오름차순 정렬
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortField).and(Sort.by(direction, "id"));

        Pageable pageable = PageRequest.of(0, size + 1, sort);

        List<Department> departments = departmentRepository.findAllByCursor(
            departmentPageRequest.nameOrDescription() == null ? "" : departmentPageRequest.nameOrDescription(),
            departmentPageRequest.idAfter(),
            isAsc,
            pageable
        );

        // 다음 페이지 여부 확인
        // 조회된 데이터의 size가 한 페이지의 데이터 size보다 크다면 다음 데이터가 더 있다는 의미
        boolean hasNext = departments.size() > size;

        // 응답에 포함할 실제 데이터 계산
        // departments로 조회한 부서 목록은 17개이지만,
        // page 요청의 size가 15개라면
        // 2개는 제외하고 15개만 반환!
        List<Department> content =
            hasNext ? departments.subList(0, size) : departments;

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
            size,
            totalElements,
            hasNext
        );
    }

    @Override
    public void delete(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new BusinessException(DepartmentErrorCode.DEPT_NOT_FOUND));

        // 부서의 직원이 1명 이상일 경우 부서 삭제 불가
        if (!department.getEmployees().isEmpty()) {
            throw new BusinessException(DepartmentErrorCode.DEPT_NOT_EMPTY);
        }

        departmentRepository.delete(department);
    }
}