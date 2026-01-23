package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupDto;
import com.sprint.mission.sb8hrbankteamquerity.dto.BuckupHistory.BackupHistoryDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import com.sprint.mission.sb8hrbankteamquerity.mapper.BackupHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.BackupHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.BackupHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BackupHistoryServiceImpl implements BackupHistoryService {

    private final BackupHistoryRepository backupHistoryRepository;
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final EmployeeRepository employeeRepository;

    private final BackupHistoryMapper backupHistoryMapper;

    @Override
    public BackupHistoryDto create() throws UnknownHostException {
        String workerIp = InetAddress.getLocalHost().getHostAddress();

        Instant lastEndedAt = backupHistoryRepository.findLatestEndedAt()
            .orElse(Instant.EPOCH);

        boolean needBackup = employeeHistoryRepository.existsByCreatedAtGreaterThanEqual(lastEndedAt);

        //백업이 불필요한 경우
        if (!needBackup) {
            return savedSkippedHisotry(workerIp);
        }

        //백업이 필요한 경우
        BackupHistory backupHistory = savedinProgressHistory(workerIp);

        try {
            log.info(">>> 백업 작업 시작 (ID: {})", backupHistory.getId());
            //csv 생성 로직 예정
            FileMeta fileMeta = generateEmployeeCsvFile(backupHistory);
            savedCompletedHistory(backupHistory,  fileMeta);

            log.info(">>> 백업 완료");

        } catch (Exception e) {
            log.error(">>> 백업 실패", e);
            failureBackupHistory(backupHistory, e);
        }

        return backupHistoryMapper.toDto(backupHistory);
    }

    @Override
    public List<BackupHistoryDto> findAll() {
        return List.of();
    }

    @Override
    public List<BackupHistoryDto> findLatestByStatus() {
        return List.of();
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

    private FileMeta generateEmployeeCsvFile(BackupHistory backupHistory) throws IOException {

        String fileName = "employee_backup_" +  convertFileName(backupHistory) + ".csv";

        //OOM 방지: 페이징으로 끊어서 조회
        int page = 0;
        int size = 1000;
        while (true) {
            Page<Employee> employeePage = employeeRepository.findAll(PageRequest.of(page, size));
            List<Employee> employees = employeePage.getContent();
        }

        FileMeta fileMeta = new FileMeta(fileName, );
        return fileMetaRespoitory.save(fileMeta);
    }

    private String convertFileName(BackupHistory backupHistory) {
        // Instant - EX) 2026-01-22T10:00:00Z
        // Instant -> ZoneId를 적용한 ZoneDateTime -> LocalDateTime
        // ZoneId.systemDefault - 한국 시간
        LocalDateTime ldt = LocalDateTime.ofInstant(backupHistory.getStartedAt(), ZoneId.systemDefault());

        LocalDate date = ldt.toLocalDate();
        LocalTime time = ldt.toLocalTime();

        String formattedDate = date.toString().replace("-", "");
        String formattedTime = time.toString().replace(":", "");

        return backupHistory.getId() + formattedDate + "_" +  formattedTime;
    }
}
