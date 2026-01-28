package com.sprint.mission.sb8hrbankteamquerity.repository;

import com.sprint.mission.sb8hrbankteamquerity.dto.dashBoard.EmployeeDistributionRequest;
import com.sprint.mission.sb8hrbankteamquerity.entity.Employee;
import com.sprint.mission.sb8hrbankteamquerity.entity.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {


    @Query(value = "SELECT e FROM Employee e JOIN FETCH e.departmentId",
        countQuery = "SELECT count(e) FROM Employee e"
    )
    Page<Employee> findAllWithDepartment(Pageable pageable);

    Boolean existsByEmail(String email);

    long countByStatus(EmployeeStatus status);

    long countByStatusAndHireDateBetween(EmployeeStatus status, LocalDate from, LocalDate to);

    @Query(value = "SELECT d.name AS groupKey, COUNT(e) AS count FROM Employee e JOIN e.departmentId d WHERE e.status=:status GROUP BY d.name")
    List<EmployeeDistributionRequest> groupDepartment(@Param("status") EmployeeStatus status);

    @Query(value = "SELECT e.position groupKey, COUNT(e) AS count FROM Employee e WHERE e.status=:status GROUP BY e.position")
    List<EmployeeDistributionRequest> groupPosition(@Param("status") EmployeeStatus status);

    @Query(value = "SELECT COUNT(e) FROM Employee e WHERE e.hireDate<=:date")
    long countEmployee(@Param("date") LocalDate to);

}

