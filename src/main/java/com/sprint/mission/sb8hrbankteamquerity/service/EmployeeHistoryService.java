package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryFilter;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;

import java.util.List;

public interface EmployeeHistoryService {
    ChangeLogDto saveEmployeeHistory(EmployeeHistorySaveRequest employeeHistorySaveRequest);

//    List<ChangeLogDto> getAllEmployeeHistory();
    List<ChangeLogDto> getAllEmployeeHistory(EmployeeHistoryFilter employeeHistoryFilter);

    ChangeLogDetailDto getEmployeeHistoryById(Long employeeHistoryId);

    Long getEmployeeHistoryCount(String fromDate, String toDate);
}
