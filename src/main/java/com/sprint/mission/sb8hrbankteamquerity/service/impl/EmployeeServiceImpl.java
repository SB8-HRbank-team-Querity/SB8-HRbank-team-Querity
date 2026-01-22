package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeSearchDto;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> findAll(EmployeeSearchDto Dto) {
        Instant from = Dto.hireDateFrom() == null ? null :
            LocalDate.parse(Dto.hireDateFrom()).atStartOfDay().toInstant(ZoneOffset.UTC);

        Instant to = Dto.hireDateTo() == null ? null :
            LocalDate.parse(Dto.hireDateTo()).atStartOfDay().toInstant(ZoneOffset.UTC);

        List<EmployeeDto> employees = employeeRepository.findAll()
            .stream()
            .filter(employee -> Dto.nameOrEmail()==null || employee.getName().contains(Dto.nameOrEmail()) || employee.getEmail().contains(Dto.nameOrEmail()))
            .filter(employee -> Dto.employeeNumber()==null || employee.getEmployeeNumber().contains(Dto.employeeNumber()))
            .filter(employee -> Dto.departmentName()==null || employee.getDepartment().getName().contains(Dto.departmentName()))
            .filter(employee -> Dto.position()==null || employee.getPosition().contains(Dto.position()))
            .filter(employee -> Dto.hireDateFrom()==null || !employee.getHireDate().isBefore(from))
            .filter(employee -> Dto.hireDateTo()==null || !employee.getHireDate().isAfter(to))
            .filter(employee -> Dto.employeeStatus()==null || employee.getStatus().equals(Dto.employeeStatus()))
            .map(employeeMapper::toDto)
            .toList();
        return employees;
    }
}
