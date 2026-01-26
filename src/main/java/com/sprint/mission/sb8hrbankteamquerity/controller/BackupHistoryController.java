package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryPageRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.CursorPageResponseBackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import com.sprint.mission.sb8hrbankteamquerity.service.BackupHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/backups")
public class BackupHistoryController {

    private final BackupHistoryService backupHistoryService;

    @PostMapping
    public ResponseEntity<BackupHistoryDto> create() {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(backupHistoryService.create(null));
    }

    @GetMapping
    public ResponseEntity<CursorPageResponseBackupHistoryDto> findAll(
        @ModelAttribute BackupHistoryPageRequest request
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(backupHistoryService.findAll(request));
    }

    @GetMapping(path = "/latest")
    public ResponseEntity<BackupHistoryDto> findLatestByStatus(
        @RequestParam(value = "status", defaultValue = "COMPLETED") BackupHistoryStatus status
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(backupHistoryService.findLatestByStatus(status));
    }
}
