package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.BackupHistoryStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long>, BackupHistoryRepositoryCustom {
    @Query("SELECT MAX(b.endedAt) FROM BackupHistory b")
    Optional<Instant> findLatestEndedAt();

    Optional<BackupHistory> findTopByStatusOrderByStartedAtDesc(BackupHistoryStatus status);
}
