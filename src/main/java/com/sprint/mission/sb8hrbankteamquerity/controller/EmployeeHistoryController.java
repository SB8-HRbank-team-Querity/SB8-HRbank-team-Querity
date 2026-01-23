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
    private final EmployeeHistoryService service;

    // 그러고 보니 화면에서 쓸거면 json 다시 변형시켜야 되네...

    @GetMapping
    public ResponseEntity<List<EmployeeHistoryResponse>> getAllEmployeeHistory() {
        List<EmployeeHistoryResponse> employeeHistoryDTOList =
            service.getAllEmployeeHistory();

        return ResponseEntity.ok(employeeHistoryDTOList);
    }


    @GetMapping("/{employeeHistoryId}")
    public ResponseEntity<EmployeeHistoryResponse> getByIdEmployeeHistory(
        @PathVariable Long employeeHistoryId
    ) {
        EmployeeHistoryResponse employeeHistoryList =
            service.getByIdEmployeeHistory(employeeHistoryId);

        return ResponseEntity.ok(employeeHistoryList);
    }
}
