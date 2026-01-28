package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard.EmployeeCountRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard.EmployeeDistributionDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard.EmployeeTrendDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard.EmployeeTrendRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.*;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;

import java.util.List;

public interface EmployeeService {
    EmployeeDto create(EmployeeCreateRequest request, Long id);

    EmployeeDto update(Long id, EmployeeUpdateRequest request, Long profileId);

    EmployeePageResponse findAll(EmployeeSearchDto Dto);

    EmployeeDto findById(Long id);

    void delete(Long id);

    long count(EmployeeCountRequest count);

    List<EmployeeDistributionDto> distribution(String groupBy, EmployeeStatus status);

    List<EmployeeTrendDto> trend(EmployeeTrendRequest request);
}

