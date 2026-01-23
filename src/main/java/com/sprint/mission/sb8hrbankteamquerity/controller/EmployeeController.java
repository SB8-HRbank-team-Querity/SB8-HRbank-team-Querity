package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.employee.*;
import com.sprint.mission.sb8hrbankteamquerity.repository.FileRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.EmployeeService;
import com.sprint.mission.sb8hrbankteamquerity.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final FileStorageService fileStorageService;
    private final FileRepository fileRepository;

    @GetMapping
    public ResponseEntity<EmployeePageResponse> findAll(EmployeeSearchDto Dto) {
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
        @RequestPart("request") EmployeeCreateRequest request,
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
        @RequestPart("request") EmployeeUpdateRequest request,
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
}

