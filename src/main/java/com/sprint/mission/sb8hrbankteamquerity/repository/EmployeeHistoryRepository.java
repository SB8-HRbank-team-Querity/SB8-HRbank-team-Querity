package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {
    //가장 최근에 배치가 완료된 시간 이후 직원 데이터가 변경이 있을 경우
    boolean existsByCreatedAtGreaterThanEqual(Instant endedAt);

    @Query("""
        SELECT e FROM EmployeeHistory e
        WHERE (:idAfter IS NULL OR e.id > :idAfter)
          AND (:employeeNumber IS NULL OR e.employeeNumber LIKE CONCAT('%', :employeeNumber, '%'))
          AND (:type IS NULL OR e.type = :type)
          AND (:memo IS NULL OR e.memo LIKE CONCAT('%', :memo, '%'))
          AND (:ipAddress IS NULL OR e.ipAddress LIKE CONCAT('%', :ipAddress, '%'))
          AND (:atFrom IS NULL OR e.createdAt >= :atFrom)
          AND (:atTo IS NULL OR e.createdAt <= :atTo)
        """)
    Page<EmployeeHistory> findAllFilter(
        @Param("idAfter") Long idAfter,
        @Param("employeeNumber") String employeeNumber,
        @Param("type") EmployeeHistoryType type,
        @Param("memo") String memo,
        @Param("ipAddress") String ipAddress,
        @Param("atFrom") Instant atFrom,
        @Param("atTo") Instant atTo,
        Pageable pageable
    );

}
