package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.employee.*;

public interface EmployeeService {
    EmployeeDto create(EmployeeCreateRequest request, Long id);

    EmployeeDto update(Long id, EmployeeUpdateRequest request, Long profileId);

    EmployeePageResponse findAll(EmployeeSearchDto Dto);

    EmployeeDto findById(Long id);
}

