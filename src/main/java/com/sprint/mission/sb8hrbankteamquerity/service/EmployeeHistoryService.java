package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryDTO;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;

public interface EmployeeHistoryService {
    boolean saveEmployeeHistory(EmployeeHistoryDTO employeeHistoryDto);
}
