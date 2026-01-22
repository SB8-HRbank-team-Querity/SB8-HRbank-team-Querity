package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.dto.BackupDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import com.sprint.mission.sb8hrbankteamquerity.entity.FileMeta;
import com.sprint.mission.sb8hrbankteamquerity.mapper.BackupHistoryMapper;
import com.sprint.mission.sb8hrbankteamquerity.repository.BackupHistoryRepository;
import com.sprint.mission.sb8hrbankteamquerity.repository.EmployeeHisotryRepository;
import com.sprint.mission.sb8hrbankteamquerity.service.BackupHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BackupHistoryServiceImpl implements BackupHistoryService {

    private final BackupHistoryRepository backupHistoryRepository;
    private final EmployeeHisotryRepository employeeHistoryRepository;

    private final BackupHistoryMapper backupHistoryMapper;

    @Override
    @Transactional
    public BackupDto create() throws UnknownHostException {
        String workerIp = InetAddress.getLocalHost().getHostAddress();

        Instant lastEndedAt = backupHistoryRepository.findLatestEndedAt()
            .orElse(Instant.EPOCH);

        boolean needBackup = employeeHistoryRepository.existsByCreatedAtGreaterThanEqual(lastEndedAt);

        //백업이 불필요한 경우
        if (!needBackup) {
            BackupHistory skippedHisotry = new BackupHistory(workerIp, BackupHistoryStatus.SKIPPED);
            skippedHisotry.complete(null);
            BackupHistory savedHisotry = backupHistoryRepository.save(skippedHisotry);
            return backupHistoryMapper.toDto(savedHisotry);
        }

        //백업이 필요한 경우
        BackupHistory backupHistory = new BackupHistory(workerIp, BackupHistoryStatus.IN_PROGRESS);
        backupHistoryRepository.save(backupHistory);

        try {
            log.info(">>> 백업 작업 시작 (ID: {})", backupHistory.getId());

            //csv 생성 로직 예정
            FileMeta fileMeta = null;

            backupHistory.complete(fileMeta);

            log.info(">>> 백업 완료");
        } catch (Exception e) {
            log.error(">>> 백업 실패", e);
            backupHistory.fail();
        }

        BackupHistory savedBackupHistory = backupHistoryRepository.save(backupHistory);

        return backupHistoryMapper.toDto(savedBackupHistory);
    }

    @Override
    public List<BackupDto> findAll() {
        return List.of();
    }

    @Override
    public List<BackupDto> findLatestByStatus() {
        return List.of();
    }
}
