package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;

import java.util.List;

public interface EmployeeHistoryService {
    EmployeeHistoryResponse saveEmployeeHistory(EmployeeHistorySaveRequest employeeHistorySaveRequest);

    List<EmployeeHistoryResponse> getAllEmployeeHistory();

    EmployeeHistoryResponse getByIdEmployeeHistory(Long employeeHistoryId);
}
