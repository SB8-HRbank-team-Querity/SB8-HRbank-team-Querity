package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.controller.docs.EmployeeHistoryApi;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.CursorPageResponseChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryFilter;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/change-logs")
public class EmployeeHistoryController implements EmployeeHistoryApi {
    private final EmployeeHistoryService employeeHistoryService;

    @GetMapping
    public ResponseEntity<CursorPageResponseChangeLogDto> getEmployeeHistory(
        @ModelAttribute EmployeeHistoryFilter filter
    ) {
        CursorPageResponseChangeLogDto employeeHistoryDTOList =
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
        @RequestParam(required = false) Instant fromDate,
        @RequestParam(required = false) Instant toDate
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            employeeHistoryService.getEmployeeHistoryCount(fromDate, toDate)
        );
    }
}
