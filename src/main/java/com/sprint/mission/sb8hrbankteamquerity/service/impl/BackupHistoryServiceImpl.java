package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.common.util.IpUtil;
import com.sprint.mission.sb8hrbankteamquerity.dto.BackupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import com.sprint.mission.sb8hrbankteamquerity.exception.BackupHistoryErrorCode;
import com.sprint.mission.sb8hrbankteamquerity.exception.BusinessException;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    private final IpUtil ipUtil;

    private static final String CSV_HEADER = "ID, 직원번호, 이름, 이메일, 부서, 직급, 입사일, 상태";
    private static final String CSV_HEAD_NAME = "employee_backup";
    private static final String CSV_CONTENT_TYPE = ".csv";

    private static final String LOG_TITLE = "[Backup Failure Log]";
    private static final String LOG_REASON_IN_PROGRESS = "Reason: ALREADY_IN_PROGRESS";
    private static final String LOG_REASON_BAD_REQUEST = "Reason: BAD_REQUEST";
    private static final String LOG_TIME = "Time: ";
    private static final String LOG_REQUESTER = "Requester: ";
    private static final String LOG_SEPERATOR = "--------------------------------------------------";
    private static final String LOG_INFO = "[Blocking Backup Info]";
    private static final String LOG_ID = "ID: ";
    private static final String LOG_START_TIME = "Started At: ";
    private static final String LOG_STATUS = "Status: ";
    private static final String LOG_CONTENT_TYPE = ".log";

    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

    @Override
    public BackupHistoryDto create(String worker) {

        /*IP 주소
         * 배치 시스템요청: sytsem
         * 관리자 요청이면: 관리자IP
         */
        String workerIp = worker == null ? ipUtil.getClientIp() : worker;

        Optional<BackupHistory> runningHistory = backupHistoryRepository.findTopByStatusOrderByStartedAtDesc(BackupHistoryStatus.IN_PROGRESS);

        if (runningHistory.isPresent()) {
            BackupHistory backupHistory = runningHistory.get();
            writeFailureLogToFile(backupHistory, LOG_REASON_IN_PROGRESS, workerIp);

            throw new BusinessException(BackupHistoryErrorCode.BACKUP_ALREADY_IN_PROGRESS);
        }

        Instant lastEndedAt = backupHistoryRepository.findLatestEndedAt()
            .orElse(Instant.EPOCH);

        boolean needBackup = employeeHistoryRepository.existsByCreatedAtGreaterThanEqual(lastEndedAt);

        //백업이 필요한 경우와 불필요한 경우 진행중인 상태는 유지되어야 함
        //추후 상태 변경
        BackupHistory backupHistory = savedinProgressHistory(workerIp);

        // 백업이 불필요한 경우
        if (!needBackup) {
            return updatedSkippedHistory(backupHistory);
        }

        // 백업이 필요한 경우
        Path tempPath = null;
        try {
            log.info(">>> 백업 작업 시작 (ID: {})", backupHistory.getId());
            String originalFileName = CSV_HEAD_NAME + "_" + convertFileName(backupHistory);

            //임시 파일 생성 (OS의 임시 폴더에 생성됨)
            //메모리에 저장하지 않고 디스크에 넣어서 사용
            tempPath = Files.createTempFile(originalFileName, CSV_CONTENT_TYPE);
            File tempFile = tempPath.toFile();

            // CSV 쓰기
            writeCsvToFile(tempFile);

            FileMeta savedFileMeta = fileStorageService.save(tempFile, originalFileName, "text/csv");

            updatedCompletedHistory(backupHistory, savedFileMeta);

            log.info(">>> 백업 완료");

        } catch (Exception e) {
            log.error(">>> 백업 실패", e);

            FileMeta errorLogFilefileMeta = writeFailureLogToFile(backupHistory, LOG_REASON_BAD_REQUEST, workerIp);
            updatedFailureHistory(backupHistory, errorLogFilefileMeta);
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

    @Override
    public BackupHistoryDto findLatestByStatus(BackupHistoryStatus status) {
        return backupHistoryRepository.findTopByStatusOrderByStartedAtDesc(status)
            .map(backupHistoryMapper::toDto)
            .orElse(null);
    }

    //스탭 단위로 트랜잭션을 나눔

    //백업 스킵
    @Transactional
    protected BackupHistoryDto updatedSkippedHistory(BackupHistory backupHistory) {
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
    protected void updatedCompletedHistory(BackupHistory backupHistory, FileMeta fileMeta) {
        backupHistory.complete(fileMeta);
        backupHistoryRepository.save(backupHistory);
    }

    //백업 실패
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void updatedFailureHistory(BackupHistory backupHistory, FileMeta fileMeta) {
        backupHistory.fail();
        if (fileMeta != null) {
            backupHistory.updateFile(fileMeta);
        }
        backupHistoryRepository.save(backupHistory);
    }

    private String convertFileName(BackupHistory backupHistory) {
        // Instant - EX) 2026-01-22T10:00:00Z
        // Instant -> ZoneId를 적용한 ZoneDateTime -> LocalDateTime
        // ZoneId.systemDefault - 한국 시간
        LocalDateTime ldt = LocalDateTime.ofInstant(backupHistory.getStartedAt(), ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        return backupHistory.getId() + "_" + ldt.format(formatter);
    }

    private void writeCsvToFile(File file) throws IOException {
        try (
            BufferedWriter writer = new BufferedWriter(new FileWriter(file))
        ) {
            writer.write(CSV_HEADER);
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

    private FileMeta writeFailureLogToFile(BackupHistory backupHistory, String reason, String requester) {

        Path tempPath = null;
        FileMeta savedFileMeta = null;

        try {
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
            String startedAt = LocalDateTime.ofInstant(backupHistory.getStartedAt(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT));

            long id = backupHistory.getId();
            BackupHistoryStatus status = backupHistory.getStatus();

            String fileName = timeStamp + LOG_CONTENT_TYPE;

            tempPath = Files.createTempFile(fileName, LOG_CONTENT_TYPE);
            File tempFile = tempPath.toFile();

            try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
            ) {
                writer.write(LOG_TITLE);
                writer.newLine();
                writer.write(reason);
                writer.newLine();
                writer.write(LOG_TIME + timeStamp);
                writer.newLine();
                writer.write(LOG_REQUESTER + requester);
                writer.newLine();
                writer.write(LOG_SEPERATOR);
                writer.newLine();
                writer.write(LOG_INFO);
                writer.newLine();
                writer.write(LOG_ID + id);
                writer.newLine();
                writer.write(LOG_START_TIME + startedAt);
                writer.newLine();
                writer.write(LOG_STATUS + status);
            }

            savedFileMeta = fileStorageService.save(tempFile, fileName, "text/plain");

            log.info("에러 로그 파일 저장 완료");
        } catch (Exception e) {
            log.warn("에러 로그 파일 생성 중 실패", e);
        } finally {
            // 에러 로그가 성공 여부와 상관없이 임시 파일 삭제
            if (tempPath != null) {
                try {
                    Files.deleteIfExists(tempPath);
                } catch (IOException e) {
                    log.warn("임시 파일 삭제 실패: {}", tempPath, e);
                }
            }
        }

        return savedFileMeta;
    }
}
