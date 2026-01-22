package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeSearchDto;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> findAll(EmployeeSearchDto Dto) {
        List<EmployeeDto> employee = employeeService.findAll(Dto);
        return ResponseEntity.status(HttpStatus.OK).body(employee);

    }
}
