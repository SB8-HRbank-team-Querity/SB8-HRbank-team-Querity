package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistoryType;
import com.sprint.mission.sb8hrbankteamquerity.entity.sortType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {
    //가장 최근에 배치가 완료된 시간 이후 직원 데이터가 변경이 있을 경우
    boolean existsByCreatedAtGreaterThanEqual(Instant endedAt);

    @Query("""
        SELECT e FROM Employee_history e
           WHERE (:employeeNumber IS NULL OR e.employee_number ILIKE CONCAT('%', :employeeNumber, '%'))
              AND (:type IS NULL OR e.type = :type)
              AND (:memo IS NULL OR e.memo ILIKE CONCAT('%', :memo, '%'))
              AND (:ipAddress IS NULL OR e.ip_address ILIKE CONCAT('%', :ipAddress, '%'))
              AND (:atFrom IS NULL OR e.created_at >= :atFrom)
              AND (:atTo IS NULL OR e.created_at <= :atTo)
              AND (:idAfter IS NULL OR e.id > :idAfter)
        
           ORDER BY
              CASE WHEN :sortField = 'createdAt' AND :sortDirection = 'ASC'
                  THEN e.created_at END ASC,
              CASE WHEN :sortField = 'createdAt' AND :sortDirection = 'DESC'
                  THEN e.created_at END DESC,
              CASE WHEN :sortField = 'id' AND :sortDirection = 'ASC'
                  THEN e.id END ASC,
              CASE WHEN :sortField = 'id' AND :sortDirection = 'DESC'
                  THEN e.id END DESC
           LIMIT :size
        """)
    List<ChangeLogDto> findAllFilter(
        @Param("type") EmployeeHistoryType type,
        @Param("employeeNumber") String employeeNumber,
        @Param("memo") String memo,
        @Param("ipAddress") String ipAddress,
        @Param("atFrom") LocalDate atFrom,
        @Param("atTo") LocalDate atTo,
        @Param("size") Long size,
        @Param("sortField") String sortField,
        @Param("sortDirection") sortType direction,
        @Param("cursor") LocalDate cursor,
        @Param("idAfter") Long idAfter
    );
}
