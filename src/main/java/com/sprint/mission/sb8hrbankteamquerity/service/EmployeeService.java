package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeSearchDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> findAll(EmployeeSearchDto Dto);
}
