package com.sprint.mission.sb8hrbankteamquerity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "backup_history")
public class BackupHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @CreatedDate
    @Column(
        name = "created_at",
        updatable = false
    )
    private Instant createdAt;

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
        //
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
