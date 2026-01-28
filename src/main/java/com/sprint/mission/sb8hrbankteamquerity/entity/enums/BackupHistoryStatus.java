package com.sprint.mission.sb8hrbankteamquerity.entity.enums;

public enum BackupHistoryStatus {
    IN_PROGRESS("진행중"),
    COMPLETED("완료"),
    SKIPPED("건너뜀"),
    FAILED("실패");

    private final String description;

    BackupHistoryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
