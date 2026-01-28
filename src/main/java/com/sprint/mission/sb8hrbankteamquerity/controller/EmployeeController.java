package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.controller.docs.EmployeeApi;
import com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard.EmployeeCountRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard.EmployeeDistributionDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard.EmployeeTrendDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard.EmployeeTrendRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.employee.*;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeStatus;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeService;
import com.sprint.mission.sb8hrbankteamquerity.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController implements EmployeeApi {

    private final EmployeeService employeeService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<EmployeePageResponse> findAll(@ParameterObject EmployeeSearchDto Dto) {
        EmployeePageResponse employee = employeeService.findAll(Dto);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> findById(@PathVariable Long id) {
        EmployeeDto employee = employeeService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<EmployeeDto> create(
        @RequestPart("employee") EmployeeCreateRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
        Long id = null;
        if (profile != null && !profile.isEmpty()) {
            id = fileStorageService.save(profile).getId();
        }
        EmployeeDto employee = employeeService.create(request, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<EmployeeDto> update(
        @PathVariable Long id,
        @RequestPart("employee") EmployeeUpdateRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

        Long profileId = null;
        if (profile != null && !profile.isEmpty()) {
            profileId = fileStorageService.save(profile).getId();
        }
        EmployeeDto employee = employeeService.update(id, request, profileId);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/trend")
    public ResponseEntity<List<EmployeeTrendDto>> getTrendEmployees(EmployeeTrendRequest request) {
        List<EmployeeTrendDto> list = employeeService.trend(request);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/stats/distribution")
    public ResponseEntity<List<EmployeeDistributionDto>> getDistributionEmployees(
        @RequestParam(value = "groupBy", defaultValue = "department") String groupBy,
        @RequestParam(value = "status", defaultValue = "ACTIVE") EmployeeStatus status
    ) {
        List<EmployeeDistributionDto> list = employeeService.distribution(groupBy, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCountEmployees(EmployeeCountRequest request) {
        long totalcount = employeeService.count(request);
        return ResponseEntity.ok(totalcount);
    }
}

