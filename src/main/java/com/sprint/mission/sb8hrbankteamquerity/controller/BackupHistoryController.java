package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import com.sprint.mission.sb8hrbankteamquerity.service.BackupHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/backups")
public class BackupHistoryController {

    private final BackupHistoryService backupHistoryService;

    @PostMapping
    public ResponseEntity<BackupHistoryDto> create() throws UnknownHostException {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(backupHistoryService.create(null));
    }

    //    @GetMapping
//    public ResponseEntity<List<BackupDto>> findAll() {
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body(backupHistoryService.findAll());
//    }
//
    @GetMapping(path = "/latest")
    public ResponseEntity<List<BackupHistoryDto>> findLatestByStatus(
        @RequestParam("status") BackupHistoryStatus status
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(backupHistoryService.findLatestByStatus(status));
    }
}
