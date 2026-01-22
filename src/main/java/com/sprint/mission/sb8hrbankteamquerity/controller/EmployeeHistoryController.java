package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryGetResponse;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employeeHistory")
public class EmployeeHistoryController {
    private final EmployeeHistoryService employeeHistoryService;

    @PostMapping
    public ResponseEntity<EmployeeHistoryGetResponse> saveEmployeeHistory(
        @RequestBody EmployeeHistorySaveRequest employeeHistorySaveRequest
    ){
        // 해당 컨트롤러는 포스트맨을 활용한 정상 동작을 확인하는 용으로
        // 요구사항에서 존재하지 않는 api입니다.
        EmployeeHistoryGetResponse employeeHistoryDTO =
            employeeHistoryService.saveEmployeeHistory(employeeHistorySaveRequest);

        return ResponseEntity.ok(employeeHistoryDTO);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeHistoryGetResponse>> getAllEmployeeHistory(){
        List<EmployeeHistoryGetResponse> employeeHistoryDTOList =
            employeeHistoryService.getAllEmployeeHistory();

        return ResponseEntity.ok(employeeHistoryDTOList);
    }


    @GetMapping(name = "")
    public ResponseEntity<EmployeeHistoryGetResponse> getByIdEmployeeHistory(
        @PathVariable Long employeeHistoryId
    ){
        EmployeeHistoryGetResponse employeeHistoryList =
            employeeHistoryService.getByIdEmployeeHistory(employeeHistoryId);

        return ResponseEntity.ok(employeeHistoryList);
    }
}
