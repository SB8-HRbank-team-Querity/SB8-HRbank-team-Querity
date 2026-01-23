package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.employee.*;
import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.DepartmentRepository;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeRepository;
import com.sprint.mission.sb8hrbankteamquerity.repository.FileRepository;
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
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final DepartmentRepository departmentRepository;
    private final FileRepository fileRepository;
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

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto findById(Long id) {
        return employeeRepository.findById(id)
            .map(employeeMapper::toDto)
            .orElseThrow(() -> new NoSuchElementException("직원을 찾을 수 없습니다."));
    }

    @Override
    public EmployeeDto create(EmployeeCreateRequest request, Long id) {
        if (employeeRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Department department = departmentRepository.findById(request.departmentId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서입니다."));

        Employee employee;
        if (id == null) {
            employee = Employee.create(request.name(), request.email(), department, request.position(), request.hireDate());
        } else {
            employee = Employee.createProfile(request.name(), request.email(), department, request.position(), request.hireDate(), id);
        }
        Employee employeeSaved = employeeRepository.save(employee);
        return employeeMapper.toDto(employeeSaved);
    }


    @Override
    public EmployeeDto update(Long id, EmployeeUpdateRequest request, Long profileId) {

        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다."));

        String name = request.name() == null ? employee.getName() : request.name();
        String email = request.email() == null ? employee.getEmail() : request.email();
        Long departmentId = request.departmentId() == null ? employee.getDepartmentId().getId() : request.departmentId();
        String position = request.position() == null ? employee.getPosition() : request.position();
        Instant hireDate = request.hireDate() == null ? employee.getHireDate() : request.hireDate();
        EmployeeStatus status = request.status() == null ? employee.getStatus() : request.status();

        Long oldProfileId = null;
        if (profileId != null) {
            oldProfileId = employee.getProfileImageId();
        }


        if (!email.equals(employee.getEmail()) && employeeRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Department department = departmentRepository.findById(departmentId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서입니다."));


        employee.update(name, email, department, position, hireDate, status, profileId);
        employeeRepository.saveAndFlush(employee);

        if (profileId != null && oldProfileId != null) {
            fileRepository.deleteById(oldProfileId);
        }

        if (request.memo() != null && !request.memo().isEmpty()) {
            // EmployeeHistoty save가 완성되면 구현할 예정입니다.
        }

        return employeeMapper.toDto(employee);
    }
}

