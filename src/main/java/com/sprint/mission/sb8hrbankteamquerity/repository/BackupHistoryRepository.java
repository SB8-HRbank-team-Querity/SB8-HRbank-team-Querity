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
public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {
    // 가장 최근에 배치가 완료된 작업 시간
    @Query("SELECT MAX(b.endedAt) FROM BackupHistory b")
    Optional<Instant> findLatestEndedAt();

    // 백업 상태에 따른 가장 최신 백업 시작 하나 가져오기
    Optional<BackupHistory> findTopByStatusOrderByStartedAtDesc(BackupHistoryStatus status);

    // 내림차순 - 최신순
    @Query("SELECT b FROM BackupHistory b " +
        "WHERE (:worker IS NULL OR b.worker LIKE :worker) " +
        "AND (:statusFilter IS NULL OR b.status = :statusFilter) " +
        "AND (cast(:startedAtFrom as timestamp) IS NULL OR b.startedAt >= :startedAtFrom) " +
        "AND (cast(:startedAtTo as timestamp) IS NULL OR b.startedAt <= :startedAtTo) " +
        "AND (" +
        "   cast(:cursorTime as timestamp) IS NULL " +
        "   OR " +
        "   (b.startedAt < :cursorTime OR (b.startedAt = :cursorTime AND b.id < :cursorId)) " +
        ")")
    List<BackupHistory> findAllByStartedAtDesc(
        @Param("cursorTime") Instant cursorTime, @Param("cursorId") Long cursorId,
        @Param("worker") String worker, @Param("statusFilter") BackupHistoryStatus statusFilter,
        @Param("startedAtFrom") Instant startedAtFrom, @Param("startedAtTo") Instant startedAtTo,
        Pageable pageable
    );

    // 오름차순 - 과거순
    @Query("SELECT b FROM BackupHistory b " +
        "WHERE (:worker IS NULL OR b.worker LIKE :worker) " +
        "AND (:statusFilter IS NULL OR b.status = :statusFilter) " +
        "AND (cast(:startedAtFrom as timestamp) IS NULL OR b.startedAt >= :startedAtFrom) " +
        "AND (cast(:startedAtTo as timestamp) IS NULL OR b.startedAt <= :startedAtTo) " +
        "AND (" +
        "   cast(:cursorTime as timestamp) IS NULL " +
        "   OR " +
        "   (b.startedAt > :cursorTime OR (b.startedAt = :cursorTime AND b.id > :cursorId)) " +
        ")")
    List<BackupHistory> findAllByStartedAtAsc(
        @Param("cursorTime") Instant cursorTime, @Param("cursorId") Long cursorId,
        @Param("worker") String worker, @Param("statusFilter") BackupHistoryStatus statusFilter,
        @Param("startedAtFrom") Instant startedAtFrom, @Param("startedAtTo") Instant startedAtTo,
        Pageable pageable
    );

    // endedAt - 내림차순
    @Query("SELECT b FROM BackupHistory b " +
        "WHERE (:worker IS NULL OR b.worker LIKE :worker) " +
        "AND (:statusFilter IS NULL OR b.status = :statusFilter) " +
        "AND (cast(:startedAtFrom as timestamp) IS NULL OR b.startedAt >= :startedAtFrom) " +
        "AND (cast(:startedAtTo as timestamp) IS NULL OR b.startedAt <= :startedAtTo) " +
        "AND (" +
        "   cast(:cursorTime as timestamp) IS NULL " +
        "   OR " +
        "   (b.endedAt < :cursorTime OR (b.endedAt = :cursorTime AND b.id < :cursorId)) " +
        ")")
    List<BackupHistory> findAllByEndedAtDesc(
        @Param("cursorTime") Instant cursorTime, @Param("cursorId") Long cursorId,
        @Param("worker") String worker, @Param("statusFilter") BackupHistoryStatus statusFilter,
        @Param("startedAtFrom") Instant startedAtFrom, @Param("startedAtTo") Instant startedAtTo,
        Pageable pageable
    );

    // endedAt - 오름차순
    @Query("SELECT b FROM BackupHistory b " +
        "WHERE (:worker IS NULL OR b.worker LIKE :worker) " +
        "AND (:statusFilter IS NULL OR b.status = :statusFilter) " +
        "AND (cast(:startedAtFrom as timestamp) IS NULL OR b.startedAt >= :startedAtFrom) " +
        "AND (cast(:startedAtTo as timestamp) IS NULL OR b.startedAt <= :startedAtTo) " +
        "AND (" +
        "   cast(:cursorTime as timestamp) IS NULL " +
        "   OR " +
        "   (b.endedAt > :cursorTime OR (b.endedAt = :cursorTime AND b.id > :cursorId)) " +
        ")")
    List<BackupHistory> findAllByEndedAtAsc(
        @Param("cursorTime") Instant cursorTime, @Param("cursorId") Long cursorId,
        @Param("worker") String worker, @Param("statusFilter") BackupHistoryStatus statusFilter,
        @Param("startedAtFrom") Instant startedAtFrom, @Param("startedAtTo") Instant startedAtTo,
        Pageable pageable
    );

    // 상태 내림차순
    @Query("SELECT b FROM BackupHistory b " +
        "WHERE (:worker IS NULL OR b.worker LIKE :worker) " +
        "AND (:statusFilter IS NULL OR b.status = :statusFilter) " +
        "AND (cast(:startedAtFrom as timestamp) IS NULL OR b.startedAt >= :startedAtFrom) " +
        "AND (cast(:startedAtTo as timestamp) IS NULL OR b.startedAt <= :startedAtTo) " +
        "AND (" +
        "   :cursorStatus IS NULL " +
        "   OR " +
        "   (b.status < :cursorStatus OR (b.status = :cursorStatus AND b.id < :cursorId)) " +
        ")")
    List<BackupHistory> findAllByStatusDesc(
        @Param("cursorStatus") BackupHistoryStatus cursorStatus, @Param("cursorId") Long cursorId,
        @Param("worker") String worker, @Param("statusFilter") BackupHistoryStatus statusFilter,
        @Param("startedAtFrom") Instant startedAtFrom, @Param("startedAtTo") Instant startedAtTo,
        Pageable pageable
    );

    // 상태 오름차순
    @Query("SELECT b FROM BackupHistory b " +
        "WHERE (:worker IS NULL OR b.worker LIKE :worker) " +
        "AND (:statusFilter IS NULL OR b.status = :statusFilter) " +
        "AND (cast(:startedAtFrom as timestamp) IS NULL OR b.startedAt >= :startedAtFrom) " +
        "AND (cast(:startedAtTo as timestamp) IS NULL OR b.startedAt <= :startedAtTo) " +
        "AND (" +
        "   :cursorStatus IS NULL " +
        "   OR " +
        "   (b.status > :cursorStatus OR (b.status = :cursorStatus AND b.id > :cursorId)) " +
        ")")
    List<BackupHistory> findAllByStatusAsc(
        @Param("cursorStatus") BackupHistoryStatus cursorStatus, @Param("cursorId") Long cursorId,
        @Param("worker") String worker, @Param("statusFilter") BackupHistoryStatus statusFilter,
        @Param("startedAtFrom") Instant startedAtFrom, @Param("startedAtTo") Instant startedAtTo,
        Pageable pageable
    );
}
