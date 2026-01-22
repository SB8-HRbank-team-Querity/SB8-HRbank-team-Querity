package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryGetResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeHistoryService {
    EmployeeHistoryGetResponse saveEmployeeHistory(EmployeeHistorySaveRequest employeeHistorySaveRequest);
    List<EmployeeHistoryGetResponse> getAllEmployeeHistory();
}
