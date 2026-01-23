package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryResponse;
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
@RequestMapping("/employeeHistory")
public class EmployeeHistoryController {
    private final EmployeeHistoryService employeeHistoryService;

    @GetMapping
    public ResponseEntity<List<EmployeeHistoryResponse>> getEmployeeHistoryById () {
        List<EmployeeHistoryResponse> employeeHistoryDTOList =
            employeeHistoryService.getAllEmployeeHistory();

        return ResponseEntity.ok(employeeHistoryDTOList);
    }


    @GetMapping("/{employeeHistoryId}")
    public ResponseEntity<EmployeeHistoryResponse> getByIdEmployeeHistory(
        @PathVariable Long employeeHistoryId
    ) {
        EmployeeHistoryResponse employeeHistoryList =
            employeeHistoryService.getByIdEmployeeHistory(employeeHistoryId);

        return ResponseEntity.ok(employeeHistoryList);
    }
}
