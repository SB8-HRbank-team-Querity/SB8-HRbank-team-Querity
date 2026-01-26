package com.sprint.mission.sb8hrbankteamquerity.component;

import com.sprint.mission.sb8hrbankteamquerity.service.BackupHistoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BackupScheduler {

    private static final Logger log = LoggerFactory.getLogger(BackupScheduler.class);

    private final BackupHistoryService backupHistoryService;

    @Scheduled(
        // 반복 주기
        fixedRateString = "${backup.scheduler.interval}",
        // 첫 실행 대기 시간
        initialDelayString = "${backup.scheduler.interval}"
    )
    public void scheduledBackup() throws UnknownHostException {
        String worker = "system";

        log.info("[{}] 데이터 백업 프로세스 시작 - Time: {}", worker, LocalDateTime.now());

        try {
            executeBackupLogic(worker);

            log.info("[{}] 데이터 백업 프로세스 성공적으로 완료", worker);
        } catch (Exception e) {
            log.error("[{}] 데이터 백업 중 오류 발생: ", worker, e);
        }
    }

    private void executeBackupLogic(String worker) throws UnknownHostException {
        log.info(">> " + worker + "권한으로 데이터를 저장소로 백업 중..........");

        backupHistoryService.create(worker);
    }
}
