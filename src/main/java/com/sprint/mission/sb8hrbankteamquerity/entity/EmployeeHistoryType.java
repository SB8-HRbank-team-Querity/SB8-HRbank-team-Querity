package com.sprint.mission.sb8hrbankteamquerity.entity;

public enum EmployeeHistoryType {
    ADD("직원 추가"),
    UPDATE("정보 수정"),
    DEL("직원 삭제");

    private final String label;

    EmployeeHistoryType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}