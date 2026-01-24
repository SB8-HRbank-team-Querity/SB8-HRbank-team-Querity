package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDetailDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.EmployeeHistorySaveRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.EmployeeDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryDiffRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import com.sprint.mission.sb8hrbankteamquerity.mapper.EmployeeHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/change-logs")
public class EmployeeHistoryController {
    private final EmployeeHistoryService employeeHistoryService;
    private final EmployeeHistoryMapper employeeHistoryMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChangeLogDto> postEmployeeHistory(
        @RequestBody EmployeeHistoryDiffRequest test
    ) {
        ChangeLogDto responseDto =  employeeHistoryService.saveEmployeeHistory(
            new EmployeeHistorySaveRequest(
                EmployeeHistoryType.CREATED, //EmployeeHistoryType 중 하나 하시면 됩니다.
                "메모내용",
                "ip 주소 값",
                employeeHistoryMapper.toChangedDetail(test.newDto(), test.oldDto()),
                "직원이름",
                "사원번호"
            ));

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

//    @GetMapping
//    public ResponseEntity<List<ChangeLogDto>> getEmployeeHistoryById() {
//        List<ChangeLogDto> employeeHistoryDTOList =
//            employeeHistoryService.getAllEmployeeHistory();
//
//        return ResponseEntity.ok(employeeHistoryDTOList);
//    }

    @GetMapping("/{employeeHistoryId}")
    public ResponseEntity<ChangeLogDetailDto> getEmployeeHistoryById(
        @PathVariable Long employeeHistoryId
    ) {
        ChangeLogDetailDto employeeHistoryList =
            employeeHistoryService.getEmployeeHistoryById(employeeHistoryId);

        return ResponseEntity.ok(employeeHistoryList);
    }
}
