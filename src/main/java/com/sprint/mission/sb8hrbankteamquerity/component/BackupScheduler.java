package com.sprint.mission.sb8hrbankteamquerity.component;

import com.sprint.mission.sb8hrbankteamquerity.service.BackupHistoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BackupScheduler {

    private static final Logger log = LoggerFactory.getLogger(BackupScheduler.class);

    private final BackupHistoryService backupHistoryService;

    private static final String WORKER = "system";

    @Scheduled(
        // 반복 주기
        fixedRateString = "${backup.scheduler.interval}",
        // 첫 실행 대기 시간
        initialDelayString = "${backup.scheduler.interval}"
    )
    public void scheduledBackup() {
        log.info("[{}] 데이터 백업 프로세스 시작 - Time: {}", WORKER, LocalDateTime.now());

        try {
            executeBackupLogic();

            log.info("[{}] 데이터 백업 프로세스 성공적으로 완료", WORKER);
        } catch (Exception e) {
            log.error("[{}] 데이터 백업 중 오류 발생: ", WORKER, e);
        }
    }

    private void executeBackupLogic() {
        log.info(">> " + WORKER + "권한으로 데이터를 저장소로 백업 중..........");

        backupHistoryService.create(WORKER);
    }

    // 크론 방법
    // 30분간격 모니터링
    @Scheduled(cron = "0 */30 * * * *")
    public void scheduledMonitorInProgress() {
        log.info("[{}] 데이터 모니터링 프로세스 시작 - Time: {}", WORKER, LocalDateTime.now());

        try {
            executeMonitorInProgress();
        } catch (Exception e) {
            log.error("[{}] 데이터 모니터링 중 오류 발생 ", WORKER, e);
        }
    }

    private void executeMonitorInProgress() {
        int updateRows = backupHistoryService.updateInProgressToFailed();
        if (updateRows > 0) {
            log.info(">> " + WORKER + "권한으로 진행중인 데이터를 실패로 처리..........");
            log.warn("총 {}건의 오래된(30분 초과) 백업 작업을 강제 종료처리했습니다.", updateRows);
        } else {
            log.info("정리할 진행중인 데이터가 없습니다.");
        }
    }
}
