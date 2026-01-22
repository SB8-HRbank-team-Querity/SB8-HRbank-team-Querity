package com.sprint.mission.sb8hrbankteamquerity.controller;

import com.sprint.mission.sb8hrbankteamquerity.controller.docs.FileMetaApi;
import com.sprint.mission.sb8hrbankteamquerity.dto.fileMeta.FileMetaResponse;
import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import com.sprint.mission.sb8hrbankteamquerity.mapper.FileMetaMapper;
import com.sprint.mission.sb8hrbankteamquerity.service.FileStorageService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController implements FileMetaApi {

    private static final String ATTACHMENT_FILENAME_FORMAT = "attachment; filename=\"%s\"";

    private final FileStorageService fileStorageService;
    private final FileMetaMapper fileMetaMapper;

    @Override
    @PostMapping
    public ResponseEntity<FileMetaResponse> uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        FileMeta savedFile = fileStorageService.save(file);
        FileMetaResponse fileMetaResponse = fileMetaMapper.toFileMetaResponse(savedFile);
        return ResponseEntity.ok(fileMetaResponse);
    }

    @Override
    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        // 파일 리소스 가져오기
        Resource resource = fileStorageService.downloadFile(fileId);
        // 파일 메타 정보 가져오기
        FileMeta fileMeta = fileStorageService.getFileMeta(fileId);

        // 파일 이름 인코딩 (한글 파일명 깨짐 방지)
        String encodedFileName = UriUtils.encode(fileMeta.getOriginName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(fileMeta.getType()))
            /* 브라우저에서 다운로드 창 띄우는 용도(탐색기 UI 혹은 downloads 폴더에 자동 저장). 이거 없으면 브라우저에서 바로열림
            * Conent-Disposition: attachment -> 이거 화면에 띄우지말고, 파일로 저장(다운로드)해라
            * filename="~~~~" -> 저장할 때 이 이름으로 저장해라.
            * */
            .header(HttpHeaders.CONTENT_DISPOSITION, String.format(ATTACHMENT_FILENAME_FORMAT, encodedFileName))
            .body(resource);
    }

}
