package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeePageResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeSearchDto;

public interface EmployeeService {
    EmployeePageResponse findAll(EmployeeSearchDto Dto);
    EmployeeDto findById(Long id);
}
