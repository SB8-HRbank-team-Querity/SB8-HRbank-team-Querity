package com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard;

// 인터페이스로 선언해야지 DB에서 값을 꺼내서 자동 매핑
public interface EmployeeDistributionRequest {
    String getGroupKey();

    long getCount();
}
