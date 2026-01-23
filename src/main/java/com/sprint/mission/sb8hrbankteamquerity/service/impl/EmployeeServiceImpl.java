package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeePageResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeSearchDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional(readOnly = true)
    public EmployeePageResponse findAll(EmployeeSearchDto Dto) {

        //페이지네이션
        int size = (Dto.size() != null && Dto.size() > 0) ? Dto.size() : 10;
        Long idAfter = Dto.idAfter() == null ? 0 : Dto.idAfter();

        // 정렬
        String sortField = Dto.sortField() == null ? "name" : Dto.sortField();
        Sort.Direction direction = "desc".equalsIgnoreCase(Dto.sortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        Instant from = Dto.hireDateFrom() == null ? null :
            LocalDate.parse(Dto.hireDateFrom()).atStartOfDay().toInstant(ZoneOffset.UTC);

        Instant to = Dto.hireDateTo() == null ? null :
            LocalDate.parse(Dto.hireDateTo()).atStartOfDay().toInstant(ZoneOffset.UTC);

        Pageable pageable = PageRequest.of(0, size + 1, sort);

        // 조회
        List<Employee> employees = employeeRepository.findAllFilter(
            idAfter, Dto.nameOrEmail(), Dto.employeeNumber(), Dto.departmentName(), Dto.position(), from, to, Dto.status(), pageable);

        boolean hasNext = employees.size() > size;
        List<EmployeeDto> content = employees.stream()
            .limit(size)
            .map(employeeMapper::toDto)
            .toList();

        Long nextIdAfter = hasNext ? content.get(content.size() - 1).id() : null;

        // 인코딩 하는 이유 = 정렬 조건이 복잡할 때 깔끔하게 관리가 가능하고 안전함
        String nextCursor = nextIdAfter == null ? null : Base64.getEncoder().encodeToString(nextIdAfter.toString().getBytes());
        int totalElements = employees.size();

        return new EmployeePageResponse(content, nextCursor, nextIdAfter, size, totalElements, hasNext);
    }

    @Transactional(readOnly = true)
    public EmployeeDto findById(Long id) {
        return employeeRepository.findById(id)
            .map(employeeMapper::toDto)
            .orElseThrow(() -> new NoSuchElementException("직원을 찾을 수 없습니다."));
    }
}
