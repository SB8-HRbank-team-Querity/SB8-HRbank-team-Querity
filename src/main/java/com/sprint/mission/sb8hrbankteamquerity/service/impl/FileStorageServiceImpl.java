package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import com.sprint.mission.sb8hrbankteamquerity.repository.FileRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private static final String UPLOAD_DIR = "${file.upload-dir}";

    private final FileRepository fileRepository;

    @Value(UPLOAD_DIR)
    private String uploadDir;

    private Path uploadPath;

    // 최초 파일 저장 디렉토리 생성
    @PostConstruct
    public void init() {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (Exception e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패 : " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public FileMeta save(MultipartFile file) throws IOException {

        // 파일명 정리 (../ 같은 경로 조작 문자 제거)
        String originName = StringUtils.cleanPath(file.getOriginalFilename());

        // 파일명 유효성 검사 (제거 잘 됐는지)
        if (originName.contains("..")) {
            throw new RuntimeException("잘못된 파일명입니다" + originName);
        }

        // 저장될 파일명 생성
        String savedName = UUID.randomUUID().toString() + "_" + originName;

        // 저장 경로 설정
        Path targetLocation = this.uploadPath.resolve(savedName);

        // 파일 저장 (원래 있던 파일은 덮어쓰기)
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 메타 정보 DB 저장
        FileMeta fileMeta = FileMeta.builder()
            .originName(originName)
            .path(targetLocation.toString())
            .type(file.getContentType())
            .size(file.getSize())
            .build();

        return fileRepository.save(fileMeta);
    }

    @Override
    public Resource downloadFile(Long fileId) {
        return null;
    }

    @Override
    public FileMeta getFileMeta(Long fileId) {
        return null;
    }
}
