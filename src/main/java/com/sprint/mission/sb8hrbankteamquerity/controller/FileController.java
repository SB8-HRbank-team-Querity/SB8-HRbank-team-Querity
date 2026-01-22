package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import com.sprint.mission.sb8hrbankteamquerity.service.FileStorageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<FileMeta> uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        FileMeta savedFile = fileStorageService.save(file);
        return ResponseEntity.ok(savedFile);
    }

}
