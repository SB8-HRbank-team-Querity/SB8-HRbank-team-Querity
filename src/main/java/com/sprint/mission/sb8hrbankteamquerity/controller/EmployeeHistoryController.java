package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/change-logs")
public class EmployeeHistoryController {
    private final EmployeeHistoryService employeeHistoryService;

    @GetMapping
    public ResponseEntity<List<ChangeLogDto>> getEmployeeHistoryById() {
        List<ChangeLogDto> employeeHistoryDTOList =
            employeeHistoryService.getAllEmployeeHistory();

        return ResponseEntity.ok(employeeHistoryDTOList);
    }

    @GetMapping("/{employeeHistoryId}")
    public ResponseEntity<ChangeLogDetailDto> getEmployeeHistoryById(
        @PathVariable Long employeeHistoryId
    ) {
        ChangeLogDetailDto employeeHistoryList =
            employeeHistoryService.getEmployeeHistoryById(employeeHistoryId);

        return ResponseEntity.ok(employeeHistoryList);
    }
}
