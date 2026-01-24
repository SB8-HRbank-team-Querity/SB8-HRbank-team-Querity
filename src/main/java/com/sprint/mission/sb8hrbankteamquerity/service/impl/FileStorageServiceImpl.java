package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import com.sprint.mission.sb8hrbankteamquerity.exception.BusinessException;
import com.sprint.mission.sb8hrbankteamquerity.exception.FileErrorCode;
import com.sprint.mission.sb8hrbankteamquerity.repository.FileRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Slf4j
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
            throw new BusinessException(FileErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    @Transactional
    public FileMeta save(MultipartFile file) throws IOException {

        // 파일명 정리 (../ 같은 경로 조작 문자 제거)
        String originName = StringUtils.cleanPath(
            Objects.requireNonNull(file.getOriginalFilename()));

        // 파일명 유효성 검사 (제거 잘 됐는지)
        if (originName.contains("..")) {
            throw new BusinessException(FileErrorCode.INVALID_FILE_NAME);
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
    @Transactional(readOnly = true)
    public Resource downloadFile(Long fileId) {
        FileMeta fileMeta = getFileMeta(fileId);
        Path filePath = Paths.get(fileMeta.getPath());

        try {
            // 파일의 uri를 통해 Resource 객체 생성
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new BusinessException(FileErrorCode.FILE_DOWNLOAD_FAILED);
            }
        } catch (MalformedURLException e) { // 잘못된 형식의 URL일 경우
            log.error("파일 경로 오류 : {}, 경로 : {}", e, fileMeta.getPath());
            throw new BusinessException(FileErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FileMeta getFileMeta(Long fileId) {
        return fileRepository.findById(fileId)
            .orElseThrow(() -> new BusinessException(FileErrorCode.FILE_NOT_FOUND));
    }
}
