package com.sprint.mission.sb8hrbankteamquerity.entity;

import com.sprint.mission.sb8hrbankteamquerity.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "backup_history")
@EntityListeners(AuditingEntityListener.class)
public class BackupHistory extends BaseEntity {

    @Column(
        name = "worker",
        length = 50,
        nullable = false
    )
    private String worker;

    @Column(
        name = "started_at",
        nullable = false
    )
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        length = 50,
        nullable = false
    )
    private BackupHistoryStatus status;

    //양방향 일대일 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "file_id",
        nullable = false
    )
    private FileMeta fileMeta;

    public BackupHistory(String worker, BackupHistoryStatus status) {
        this.worker = worker;
        this.status = status;
        this.startedAt = Instant.now();
    }

    public void complete(FileMeta fileMeta) {
        this.status = BackupHistoryStatus.COMPLETED;
        this.endedAt = Instant.now();
        this.fileMeta = fileMeta;
    }

    public void fail() {
        this.status = BackupHistoryStatus.FAILED;
        this.endedAt = Instant.now();
    }

    public void skip() {
        this.status = BackupHistoryStatus.SKIPPED;
        this.endedAt = Instant.now();
    }
}
