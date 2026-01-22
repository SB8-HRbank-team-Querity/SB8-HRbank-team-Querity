package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeePageResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeSearchDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional(readOnly = true)
    public EmployeePageResponse findAll(EmployeeSearchDto Dto) {

        //페이지네이션
        int size = Dto.size() > 0 ? Dto.size() : 10;
        Long idAfter = Dto.idAfter() == null ? 0 : Dto.idAfter();

        // 정렬
        String sortField = Dto.sortField() == null ? "name" : Dto.sortField();

        Comparator<Employee> comparator = switch (sortField) {
            case "employeeNumber" -> Comparator.comparing(Employee::getEmployeeNumber);
            case "hireDate" -> Comparator.comparing(Employee::getHireDate);
            default -> Comparator.comparing(Employee::getName);
        };

        if ("desc".equalsIgnoreCase(Dto.sortDirection())) {
            comparator = comparator.reversed();
        }


        Instant from = Dto.hireDateFrom() == null ? null :
            LocalDate.parse(Dto.hireDateFrom()).atStartOfDay().toInstant(ZoneOffset.UTC);

        Instant to = Dto.hireDateTo() == null ? null :
            LocalDate.parse(Dto.hireDateTo()).atStartOfDay().toInstant(ZoneOffset.UTC);

        // 조회
        List<Employee> employees = employeeRepository.findAll()
            .stream()
            .filter(employee -> idAfter <= 0 || employee.getId() > idAfter)
            .filter(employee -> Dto.nameOrEmail() == null || employee.getName().contains(Dto.nameOrEmail()) || employee.getEmail().contains(Dto.nameOrEmail()))
            .filter(employee -> Dto.employeeNumber() == null || employee.getEmployeeNumber().contains(Dto.employeeNumber()))
            .filter(employee -> Dto.departmentName() == null || employee.getDepartmentId().getName().contains(Dto.departmentName()))
            .filter(employee -> Dto.position() == null || employee.getPosition().contains(Dto.position()))
            .filter(employee -> Dto.hireDateFrom() == null || !employee.getHireDate().isBefore(from))
            .filter(employee -> Dto.hireDateTo() == null || !employee.getHireDate().isAfter(to))
            .filter(employee -> Dto.status() == null || employee.getStatus().equals(Dto.status()))
            .sorted(comparator)
            .toList();

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

}
