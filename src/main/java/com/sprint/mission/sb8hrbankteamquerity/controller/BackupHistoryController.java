package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.dto.BuckupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.service.BackupHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/backups")
public class BackupHistoryController {

    private final BackupHistoryService backupHistoryService;

    @PostMapping
    public ResponseEntity<BackupHistoryDto> create() throws UnknownHostException {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(backupHistoryService.create());
    }

//    @GetMapping
//    public ResponseEntity<List<BackupDto>> findAll() {
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body(backupHistoryService.findAll());
//    }
//
//    @GetMapping
//    public ResponseEntity<List<BackupDto>> findLatestByStatus(
//        @RequestParam("status") String status
//    ) {
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body(backupHistoryService.findLatestByStatus());
//    }
}
