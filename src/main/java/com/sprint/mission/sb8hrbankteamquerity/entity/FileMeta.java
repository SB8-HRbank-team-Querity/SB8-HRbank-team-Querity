package com.sprint.mission.sb8hrbankteamquerity.entity;

import com.sprint.mission.sb8hrbankteamquerity.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_meta")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileMeta extends BaseUpdatableEntity {

    @Column(name = "origin_name", nullable = false, length = 50)
    private String originName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false, length = 100)
    private String type;

    @Column(nullable = false, length = 500)
    private String path;

    @Builder
    public FileMeta(String originName, Long size, String type, String path) {
        this.originName = originName;
        this.size = size;
        this.type = type;
        this.path = path;
    }
}
