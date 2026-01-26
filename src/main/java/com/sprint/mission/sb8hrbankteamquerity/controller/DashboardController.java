package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class DashboardController {

    // 실행하면 대시보드 에러가 나와서 임시로 만들었습니다
    @GetMapping("/stats/trend")
    public ResponseEntity<List<Employee>> getTrendEmployees(){
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/stats/distribution")
    public ResponseEntity<List<Employee>> getDistributionEmployees(){
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/count")
    public ResponseEntity<List<Employee>> getCountEmployees(){
        return ResponseEntity.ok(List.of());
    }

}
