package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.dto.EmployeeHistory.ChangeLogDto;
import com.sprint.mission.sb8hrbankteamquerity.entity.EmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {
    //가장 최근에 배치가 완료된 시간 이후 직원 데이터가 변경이 있을 경우
    boolean existsByCreatedAtGreaterThanEqual(Instant endedAt);

//    @Query("SELECT e FROM Employee_history e " +
//        "WHERE (:idAfter IS NULL OR e.id > :idAfter) " +
//        "AND (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%) " +
//        "AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%) " +
//        "AND (:departmentName IS NULL OR e.departmentId.name LIKE %:departmentName%) " +
//        "AND (:position IS NULL OR e.position LIKE %:position%) " +
//        "AND (e.hireDate >= COALESCE(:hireDateFrom, e.hireDate)) " +
//        "AND (e.hireDate <= COALESCE(:hireDateTo, e.hireDate)) " +
//        "AND (:status IS NULL OR e.status = :status)")
//    List<ChangeLogDto> findAllFilter();
}
