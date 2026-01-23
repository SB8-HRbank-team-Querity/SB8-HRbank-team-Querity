package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {
    //가장 최근에 배치가 완료된 시간 이후 직원 데이터가 변경이 있을 경우
    boolean existsByCreatedAtGreaterThanEqual(Instant endedAt);
}
