package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/change-logs")
public class EmployeeHistoryController {
    private final EmployeeHistoryService employeeHistoryService;

    @GetMapping
    public ResponseEntity<List<ChangeLogDto>> getEmployeeHistoryById () {
        List<ChangeLogDto> employeeHistoryDTOList =
            employeeHistoryService.getAllEmployeeHistory();

        return ResponseEntity.ok(employeeHistoryDTOList);
    }

    @GetMapping("/{employeeHistoryId}")
    public ResponseEntity<ChangeLogDto> getEmployeeHistoryById(
        @PathVariable Long employeeHistoryId
    ) {
        ChangeLogDto employeeHistoryList =
            employeeHistoryService.getEmployeeHistoryById(employeeHistoryId);

        return ResponseEntity.ok(employeeHistoryList);
    }
}
