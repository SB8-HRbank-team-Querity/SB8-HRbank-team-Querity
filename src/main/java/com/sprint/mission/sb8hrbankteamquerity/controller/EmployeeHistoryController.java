package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryFilter;
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
    public ResponseEntity<List<ChangeLogDto>> getEmployeeHistoryById(
        @ModelAttribute EmployeeHistoryFilter filter
    ) {
        List<ChangeLogDto> employeeHistoryDTOList =
            employeeHistoryService.getAllEmployeeHistory(filter);

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

    @GetMapping("/count")
    public ResponseEntity<Long> getEmployeeHistoryCount(
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate
    ){
        return  ResponseEntity.status(HttpStatus.OK).body(
            employeeHistoryService.getEmployeeHistoryCount(fromDate,toDate)
        );
    }
}
