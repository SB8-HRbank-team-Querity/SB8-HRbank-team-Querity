package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import com.sprint.mission.sb8hrbankteamquerity.mapper.BackupHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.BackupHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.BackupHistoryService;
import com.sprint.mission.sb8hrbankteamquerity.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BackupHistoryServiceImpl implements BackupHistoryService {

    private final BackupHistoryRepository backupHistoryRepository;
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final EmployeeRepository employeeRepository;

    private final BackupHistoryMapper backupHistoryMapper;
    private final FileStorageService fileStorageService;

    @Override
    public BackupHistoryDto create(String worker) throws UnknownHostException {

        Optional<BackupHistory> runningHistory = backupHistoryRepository.findTopByStatusOrderByStartedAtDesc(BackupHistoryStatus.IN_PROGRESS);

        // 예외처리 추후 수정 예정
        if (runningHistory.isPresent()) {
            throw new IllegalStateException("이미 진행중인 백업이 존재합니다. (ID: " + runningHistory.get().getId() + ")");
        }

        /*IP 주소
        * 배치 시스템요청: sytsem
        * 관리자 요청이면: 관리자IP
        */
        String workerIp = worker;

        if (worker == null) {
            workerIp = InetAddress.getLocalHost().getHostAddress();
        }

        Instant lastEndedAt = backupHistoryRepository.findLatestEndedAt()
            .orElse(Instant.EPOCH);

        boolean needBackup = employeeHistoryRepository.existsByCreatedAtGreaterThanEqual(lastEndedAt);

        //백업이 불필요한 경우
//        if (!needBackup) {
//            BackupHistory backupHistory = savedinProgressHistory(workerIp);
//            String fileName = "employee_backup_" +  convertFileName(backupHistory) + ".csv";
//            System.out.println("fileName = " + fileName);
//            return savedSkippedHisotry(workerIp);
//        }

        //백업이 필요한 경우
        BackupHistory backupHistory = savedinProgressHistory(workerIp);

        Path tempPath = null;

        try {
            log.info(">>> 백업 작업 시작 (ID: {})", backupHistory.getId());

            String originalFileName = "employee_backup_" +  convertFileName(backupHistory) + ".csv";
            //임시 파일 생성 (OS의 임시 폴더에 생성됨)
            //메모리에 저장하지 않고 디스크에 넣어서 사용
            tempPath = Files.createTempFile(originalFileName, ".csv");
            File tempFile = tempPath.toFile();

            // CSV 쓰기
            writeCsvToFile(tempFile);

            // 어댑터 생성 및 저장
            MultipartFile multipartFile = new FileToMultipartFileAdapter(tempFile, originalFileName, "text/csv");
            FileMeta savedFileMeta = fileStorageService.save(multipartFile);

            savedCompletedHistory(backupHistory,  savedFileMeta);

            log.info(">>> 백업 완료");

        } catch (Exception e) {
            log.error(">>> 백업 실패", e);
            failureBackupHistory(backupHistory, e);
        } finally {
            // 백업 성공 실패 상관 없이 임시 파일 삭제
            if (tempPath != null) {
                try {
                    Files.deleteIfExists(tempPath);
                } catch (IOException e) {
                    log.warn("임시 파일 삭제 실패: {}", tempPath, e);
                }
            }
        }

        return backupHistoryMapper.toDto(backupHistory);
    }

    @Override
    public List<BackupHistoryDto> findAll() {
        return List.of();
    }

    // 추후 페이지 네이션 구현
    @Override
    public List<BackupHistoryDto> findLatestByStatus(BackupHistoryStatus status) {
        return backupHistoryRepository.findAllByStatusOrderByStartedAtDesc(status).stream()
            .map(backupHistoryMapper::toDto)
            .toList();
    }

    //스탭 단위로 트랜잭션을 나눔

    //백업 스킵
    @Transactional
    protected BackupHistoryDto savedSkippedHisotry(String workerIp) {
        BackupHistory backupHistory = new BackupHistory(workerIp, BackupHistoryStatus.SKIPPED);
        backupHistory.skip();
        return backupHistoryMapper.toDto(backupHistoryRepository.save(backupHistory));
    }

    //백업 진행중
    //메서드가 호출되자마자 독립적인 트랜잭션을 생성, 끝나면 즉시 DB에 진행중 삽입
    //CSV 파일을 만드는 동안 "진행중"
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected BackupHistory savedinProgressHistory(String workerIp) {
        BackupHistory backupHistory = new BackupHistory(workerIp, BackupHistoryStatus.IN_PROGRESS);
        return backupHistoryRepository.save(backupHistory);
    }

    //백업 성공
    @Transactional
    protected void savedCompletedHistory(BackupHistory backupHistory, FileMeta fileMeta) {
        backupHistory.complete(fileMeta);
        backupHistoryRepository.save(backupHistory);
    }

    //백업 실패
    @Transactional
    protected void failureBackupHistory(BackupHistory backupHistory, Exception e) {
        backupHistory.fail();
        backupHistoryRepository.save(backupHistory);
    }

    private String convertFileName(BackupHistory backupHistory) {
        // Instant - EX) 2026-01-22T10:00:00Z
        // Instant -> ZoneId를 적용한 ZoneDateTime -> LocalDateTime
        // ZoneId.systemDefault - 한국 시간
        LocalDateTime ldt = LocalDateTime.ofInstant(backupHistory.getStartedAt(), ZoneId.systemDefault());

        LocalDate date = ldt.toLocalDate();
        LocalTime time = ldt.toLocalTime();
        // 년월일 - 20260123
        String formattedDate = date.toString().replace("-", "");
        // 시간에서 초까지만
        String formattedTime = time.toString().replace(":", "").substring(0, 6);

        return backupHistory.getId() + formattedDate + "_" +  formattedTime;
    }

    // FileStorageService를 수정하지 않기 위한 어뎁터 클래스
    // java.io.File 객체를 스프링의 MultipartFile 인터페이스로 감쌈.
    // MultipartFile은 인터페이스이기 때문에 @Override 꼭 필수!!!!!!!!!!!!!!!!!!!!!!
    private static class FileToMultipartFileAdapter implements MultipartFile {
        // 실제 디스크에 저장된 임시 파일
        private final File file;
        // 가짜 파일명
        private final String fileName;
        // 파일 타입 (text/csv)
        private final String contentType;

        public FileToMultipartFileAdapter(File file, String fileName, String contentType) {
            this.file = file;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        @Override
        public String getName() { return "file"; }

        //EX) employee_backup_....csv
        @Override
        public String getOriginalFilename() {return this.fileName; }

        @Override
        public String getContentType() { return this.contentType; }

        @Override
        public boolean isEmpty() { return file.length() == 0; }

        @Override
        public long getSize() { return file.length(); }

        //
        @Override
        public byte[] getBytes() throws IOException {
            // 이 메서드는 가급적으로 피해하는 것이 좋음. FileStorageService에서 이 메서드를 사용 시 OOM 발생
            return Files.readAllBytes(file.toPath());
        }

        // 임시 파일인지 실제 파일인지 확인해서 보내줌
        @Override
        public InputStream getInputStream() throws IOException {

            return new FileInputStream(file);
        }

        //Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

    }

    private void writeCsvToFile(File file) throws IOException {
        try (
            BufferedWriter writer = new BufferedWriter(new FileWriter(file))
        ) {
            writer.write("ID, 직원번호, 이름, 이메일, 부서, 직급, 입사일, 상태");
            writer.newLine();

            //OOM 방지: 페이징으로 끊어서 조회
            int page = 0;
            int size = 1000;

            while (true) {
                Page<Employee> employeePage = employeeRepository.findAllWithDepartment(PageRequest.of(page, size));
                List<Employee> employees = employeePage.getContent();

                if (employees.isEmpty()) break;

                for (Employee e : employees) {
                    // 20260123 (년월일) - 시간 제거
                    LocalDate localDate = e.getHireDate().atZone(ZoneId.systemDefault()).toLocalDate();
                    String departmentName = e.getDepartmentId() != null ? e.getDepartmentId().getName() : "";

                    String line = String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                        e.getId(),
                        e.getEmployeeNumber(),
                        e.getName(),
                        e.getEmail(),
                        departmentName,
                        e.getPosition(),
                        localDate,
                        e.getStatus()
                    );
                    writer.write(line);
                    writer.newLine();
                }
                page++;
            }
        }
    }
}
