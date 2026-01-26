package com.sprint.mission.sb8hrbankteamquerity.service;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.*;

import java.time.Instant;

public interface EmployeeHistoryService {
    ChangeLogDto saveEmployeeHistory(EmployeeHistorySaveRequest employeeHistorySaveRequest);

    CursorPageResponseChangeLogDto getAllEmployeeHistory(EmployeeHistoryFilter employeeHistoryFilter);

    ChangeLogDetailDto getEmployeeHistoryById(Long employeeHistoryId);

    Long getEmployeeHistoryCount(Instant fromDate, Instant toDate);
}
