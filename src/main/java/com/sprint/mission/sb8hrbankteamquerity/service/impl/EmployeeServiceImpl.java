package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.common.util.IpUtil;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.*;
import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import com.sprint.mission.sb8hrbankteamquerity.exception.BusinessException;
import com.sprint.mission.sb8hrbankteamquerity.exception.DepartmentErrorCode;
import com.sprint.mission.sb8hrbankteamquerity.exception.EmployeeErrorCode;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.DepartmentRepository;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeRepository;
import com.sprint.mission.sb8hrbankteamquerity.repository.FileRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
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

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeHistoryService employeeHistoryService;
    private final EmployeeHistoryMapper employeeHistoryMapper;
    private final DepartmentRepository departmentRepository;
    private final FileRepository fileRepository;
    private final EmployeeMapper employeeMapper;
    private final IpUtil ipUtil;


    @Override
    @Transactional(readOnly = true)
    public EmployeePageResponse findAll(EmployeeSearchDto Dto) {

        //페이지네이션
        int size = (Dto.size() != null && Dto.size() > 0) ? Dto.size() : 10;


        Long idAfter = 0L;
        if (Dto.cursor() != null && !Dto.cursor().isEmpty()) {
            try {
                String decode = new String(Base64.getDecoder().decode(Dto.cursor()));
                idAfter = Long.parseLong(decode);
            } catch (Exception e) { // 잘못된 커서 값일 경우 처음부터 조회
                idAfter = 0L;
            }
        } else if (Dto.idAfter() != null) {
            idAfter = Dto.idAfter();
        }

        // 정렬
        String sortField = Dto.sortField() == null ? "name" : Dto.sortField();
        Sort.Direction direction = "desc".equalsIgnoreCase(Dto.sortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        LocalDate from = Dto.hireDateFrom() == null ? null :
            LocalDate.parse(Dto.hireDateFrom());

        LocalDate to = Dto.hireDateTo() == null ? null :
            LocalDate.parse(Dto.hireDateTo());

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
            .orElseThrow(() -> new BusinessException(EmployeeErrorCode.EMP_NOT_FOUND));
    }

    @Override
    public EmployeeDto create(EmployeeCreateRequest request, Long id) {
        if (employeeRepository.existsByEmail(request.email())) {
            throw new BusinessException(EmployeeErrorCode.EMP_DUPLICATE_EMAIL);
        }
        Department department = departmentRepository.findById(request.departmentId())
            .orElseThrow(() -> new BusinessException(DepartmentErrorCode.DEPT_NOT_FOUND));

        Employee employee;
        if (id == null) {
            employee = Employee.create(request.name(), request.email(), department, request.position(), request.hireDate());
        } else {
            employee = Employee.createProfile(request.name(), request.email(), department, request.position(), request.hireDate(), id);
        }
        Employee employeeSaved = employeeRepository.save(employee);
        EmployeeDto newDto = employeeMapper.toDto(employeeSaved);

        employeeHistoryService.saveEmployeeHistory(
            new EmployeeHistorySaveRequest(
                EmployeeHistoryType.CREATED,
                request.memo(),
                ipUtil.getClientIp(),
                employeeHistoryMapper.toChangedDetail(newDto,null),
                employee.getName(),
                employee.getEmployeeNumber()
            ));

        return newDto;
    }


    @Override
    @Transactional
    public EmployeeDto update(Long id, EmployeeUpdateRequest request, Long profileId) {

        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new BusinessException(EmployeeErrorCode.EMP_NOT_FOUND));
        EmployeeDto oldDto = employeeMapper.toDto(employee);

        Long oldProfileId = null;
        if (profileId != null) {
            oldProfileId = employee.getProfileImageId();
        }

        if (!request.email().equals(employee.getEmail()) && employeeRepository.existsByEmail(request.email())) {
            throw new BusinessException(EmployeeErrorCode.EMP_DUPLICATE_EMAIL);
        }
        Department department = departmentRepository.findById(request.departmentId())
            .orElseThrow(() -> new BusinessException(DepartmentErrorCode.DEPT_NOT_FOUND));

        employee.update(request.name(), request.email(), department, request.position(), request.hireDate(), request.status(), profileId);
        employeeRepository.saveAndFlush(employee);
        EmployeeDto newDto = employeeMapper.toDto(employee);

        if (profileId != null && oldProfileId != null) {
            fileRepository.deleteById(oldProfileId);
        }

        if (request.memo() != null && !request.memo().isEmpty()) {
            employeeHistoryService.saveEmployeeHistory(
                new EmployeeHistorySaveRequest(
                    EmployeeHistoryType.UPDATED,
                    request.memo(),
                    ipUtil.getClientIp(),
                    employeeHistoryMapper.toChangedDetail(newDto, oldDto),
                    employee.getName(),
                    employee.getEmployeeNumber()
                ));
        }

        return employeeMapper.toDto(employee);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new BusinessException(EmployeeErrorCode.EMP_NOT_FOUND));

        employeeRepository.deleteById(id);
        employeeRepository.flush();

        if (employee.getProfileImageId() != null) {
            fileRepository.deleteById(employee.getProfileImageId());
        }
    }
}

