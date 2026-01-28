package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.common.util.IpUtil;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard.*;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.*;
import com.sprint.mission.sb8hrbankteamquerity.entity.Department;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.enums.EmployeeHistoryType;
import com.sprint.mission.sb8hrbankteamquerity.entity.enums.EmployeeStatus;
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
import com.sprint.mission.sb8hrbankteamquerity.service.Specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static com.sprint.mission.sb8hrbankteamquerity.entity.enums.EmployeeStatus.ACTIVE;

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

        // 페이지 번호
        int pageNumber = 0;
        if (Dto.idAfter() != null) {
            pageNumber = Dto.idAfter().intValue();
        } else if (Dto.cursor() != null && !Dto.cursor().isEmpty()) {
            try {
                String decode = new String(Base64.getDecoder().decode(Dto.cursor()));
                pageNumber = Integer.parseInt(decode);
            } catch (Exception e) { // 잘못된 커서 값일 경우 처음부터 조회
                pageNumber = 0;
            }
        }

        // 정렬
        String sortField = Dto.sortField() == null ? "name" : Dto.sortField();
        Sort.Direction direction = "desc".equalsIgnoreCase(Dto.sortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // 이름 or 부서명 or 입사일이 같을 경우 id를 기준으로 정렬
        Sort sort = Sort.by(direction, sortField).and(Sort.by(Sort.Direction.ASC, "id"));

        LocalDate from = (Dto.hireDateFrom() == null || Dto.hireDateFrom().isBlank()) ? null : LocalDate.parse(Dto.hireDateFrom());
        LocalDate to = (Dto.hireDateTo() == null || Dto.hireDateTo().isBlank()) ? null : LocalDate.parse(Dto.hireDateTo());

        // 조회
        Specification<Employee> specification = Specification
            .where(EmployeeSpecification.nameOrEmailContains(Dto.nameOrEmail()))
            .and(EmployeeSpecification.employeeNumberContains(Dto.employeeNumber()))
            .and(EmployeeSpecification.departmentNameContains(Dto.departmentName()))
            .and(EmployeeSpecification.positionContains(Dto.position()))
            .and(EmployeeSpecification.hireDateBetween(from, to))
            .and(EmployeeSpecification.statusEquals(Dto.status()));

        Pageable pageable = PageRequest.of(pageNumber, size, sort);
        Page<Employee> employees = employeeRepository.findAll(specification, pageable);

        Long totalElements = employees.getTotalElements();

        List<EmployeeDto> content = employees.getContent().stream()
            .map(employeeMapper::toDto)
            .toList();
        boolean hasNext = employees.hasNext();

        // 다음 페이지 요청 값
        Long nextIdAfter = hasNext ? (long) (pageNumber + 1) : null;

        // 인코딩 하는 이유 = 정렬 조건이 복잡할 때 깔끔하게 관리가 가능하고 안전함
        String nextCursor = nextIdAfter == null ? null : Base64.getEncoder().encodeToString(nextIdAfter.toString().getBytes());

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
                employee.getProfileImageId(),
                employeeHistoryMapper.toChangedDetail(newDto, null),
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

        employeeHistoryService.saveEmployeeHistory(
            new EmployeeHistorySaveRequest(
                EmployeeHistoryType.UPDATED,
                request.memo(),
                ipUtil.getClientIp(),
                employee.getProfileImageId(),
                employeeHistoryMapper.toChangedDetail(newDto, oldDto),
                employee.getName(),
                employee.getEmployeeNumber()
            ));
        return employeeMapper.toDto(employee);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new BusinessException(EmployeeErrorCode.EMP_NOT_FOUND));

        EmployeeDto oldDto = employeeMapper.toDto(employee);

        employeeHistoryService.saveEmployeeHistory(
            new EmployeeHistorySaveRequest(
                EmployeeHistoryType.DELETED,
                null,
                ipUtil.getClientIp(),
                employee.getProfileImageId(),
                employeeHistoryMapper.toChangedDetail(null, oldDto),
                employee.getName(),
                employee.getEmployeeNumber()
            ));

        employeeRepository.deleteById(id);
        employeeRepository.flush();

        if (employee.getProfileImageId() != null) {
            fileRepository.deleteById(employee.getProfileImageId());
        }
    }

    @Override
    public long count(EmployeeCountRequest request) {
        LocalDate from = request.fromDate();
        LocalDate to = request.toDate() == null ? LocalDate.now() : request.toDate();

        if (from != null) { // 이번달 신규 입사자 수
            return employeeRepository.countByStatusAndHireDateBetween(ACTIVE, from, to);
        }
        if (request.status() != null) { // 총 재직자 수
            return employeeRepository.countByStatus(request.status());
        }
        return employeeRepository.count();
    }

    @Override
    public List<EmployeeDistributionDto> distribution(String groupBy, EmployeeStatus status) {
        List<EmployeeDistributionRequest> list;

        if ("position".equals(groupBy)) {  // 직무별
            list = employeeRepository.groupPosition(status);
        } else { // 부서별
            list = employeeRepository.groupDepartment(status);
        }

        long count = list.stream()
            .mapToLong(EmployeeDistributionRequest::getCount).sum();

        return list.stream()
            .map(request -> {
                double percentage = count == 0 ? 0 : (double) request.getCount() / count * 100;
                return new EmployeeDistributionDto(request.getGroupKey(), (int) request.getCount(), percentage);
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeTrendDto> trend(EmployeeTrendRequest request) {
        LocalDate to = request.to() == null ? LocalDate.now() : request.to();
        String unit = request.unit() == null ? "month" : request.unit();

        List<EmployeeTrendDto> list = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();

        // 데이터 가져오기
        for (int i = 12; i >= 0; i--) {
            LocalDate date = calculateDate(to, unit, i);
            long total = employeeRepository.countEmployee(date);
            counts.add(total);
            dates.add(date);
        }

        // 계산
        for (int i = 1; i < counts.size(); i++) {
            long currentCount = counts.get(i);
            long previousCount = counts.get(i - 1);

            long change = currentCount - previousCount;
            double changeRate = (previousCount == 0) ? 0 : ((double) change / previousCount) * 100;
            changeRate = Math.round(changeRate * 100.0) / 100.0;

            list.add(new EmployeeTrendDto(
                dates.get(i),
                currentCount,
                change,
                changeRate
            ));
        }
        return list;
    }

    private LocalDate calculateDate(LocalDate to, String unit, int i) {
        return switch (unit) {
            case "day" -> to.minusDays(i);
            case "week" -> to.minusWeeks(i);
            case "quarter" -> to.minusMonths(i * 3).with(TemporalAdjusters.lastDayOfMonth());
            case "year" -> to.minusYears(i).with(TemporalAdjusters.lastDayOfMonth());
            default -> to.minusMonths(i).with(TemporalAdjusters.lastDayOfMonth());
        };
    }
}
