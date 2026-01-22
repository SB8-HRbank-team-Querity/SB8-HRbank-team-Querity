package com.sprint.mission.sb8hrbankteamquerity.entity;

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
    private HistoryStatus status;

    //양방향 일대일 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "file_id",
        nullable = false
    )
    private FileMeta fileMeta;

    public BackupHistory(String worker, HistoryStatus status, FileMeta fIleMeta) {
        this.worker = worker;
        this.status = status;
        this.fileMeta = fIleMeta;
    }

    public void update(String newWorker, HistoryStatus newStatus) {
        if (newWorker != null && !newWorker.equals(this.worker)) {
            this.worker = newWorker;
        }
        if (newStatus != null && !newStatus.equals(this.status)) {
            this.status = newStatus;
        }
    }
}
