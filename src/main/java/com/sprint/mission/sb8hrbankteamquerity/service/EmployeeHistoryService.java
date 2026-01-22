package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryDTO;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import org.springframework.http.ResponseEntity;

public interface EmployeeHistoryService {
    ResponseEntity<EmployeeHistoryDTO> saveEmployeeHistory(EmployeeHistorySaveRequest employeeHistorySaveRequest);
}
