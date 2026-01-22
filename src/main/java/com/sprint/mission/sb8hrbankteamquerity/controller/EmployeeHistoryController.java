package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistoryDTO;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employeeHistory")
public class EmployeeHistoryController {
    private final EmployeeHistoryService employeeHistoryService;

    @PostMapping
    public boolean getEmployeeHistory(
        @RequestBody EmployeeHistorySaveRequest employeeHistorySaveRequest
    ){
        // 해당 컨트롤러는 포스트맨을 활용한 정상 동작을 확인하는 용으로
        // 요구사항에서 존재하지 않는 api입니다.
        return employeeHistoryService.saveEmployeeHistory(employeeHistorySaveRequest);
    }
}
